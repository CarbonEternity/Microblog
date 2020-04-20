package database;

import entities.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLManager {

    public static final String INSERT_INTO_POSTS_USERNAME_POST_TEXT_VALUES = "INSERT INTO posts(username, post_text) VALUES (?,?)";
    public static final String SELECT_FROM_POSTS_ORDER_BY_POST_ID_DESC_LIMIT_OFFSET_1 = "SELECT * FROM posts ORDER BY post_id DESC LIMIT ? OFFSET (?-1)*? ;";
    public static final String SELECT_COUNT_AS_NUMBER_FROM_POSTS = "SELECT COUNT(*) AS number FROM posts;";

    private Connection openConnection() {
        String driver = "org.postgresql.Driver";
        String connectionString = "jdbc:postgresql://localhost:5432/service";
        String connectionUsername = "postgres";
        String connectionPassword = "postgres";
        try {
            Class.forName(driver);
            return DriverManager.getConnection(
                    connectionString,
                    connectionUsername,
                    connectionPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void insertIntoPosts(String name, String text) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt;
        try {
            conn = openConnection();
            assert conn != null;
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(INSERT_INTO_POSTS_USERNAME_POST_TEXT_VALUES);
            stmt.setString(1, name);
            stmt.setString(2, text);


            stmt.executeUpdate();
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.close();
        }
    }

/*Записи з бази даних витягуються в зворотньому
порядку їх додавання − спочатку останні опубліковані.*/


/*Параметр postsPerPage методу selectPosts визначає, скільки записів необхідно
розташувати на одній сторінці. Параметр pageNumber методу selectPosts
визначає номер рядка, з якого слід почати вибірку.*/

    public List<Post> selectPosts(int pageNumber, int postsPerPage) throws SQLException {
        Connection conn = null;
        List<Post> notes = new ArrayList<>();
        PreparedStatement pstmt ;
        try {
            conn = openConnection();
            assert conn != null;
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SELECT_FROM_POSTS_ORDER_BY_POST_ID_DESC_LIMIT_OFFSET_1);

            pstmt.setInt(1, postsPerPage);
            pstmt.setInt(2, pageNumber);
            pstmt.setInt(3, postsPerPage);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Post note = new Post();
                note.setDate(rs.getTimestamp("publish_date"));
                note.setUsername(rs.getString("username"));
                note.setText(rs.getString("post_text"));
                notes.add(note);
            }

            rs.close();
            pstmt.close();
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.close();
        }
        return notes;
    }





    /*Для отримання інформації про те, скільки всього записів міститься в
таблиці, можна створити метод countPosts*/

    public int countPosts() throws SQLException {
        int postNumber = 0;
        Connection conn = null;
        try {
            conn = openConnection();
            assert conn != null;
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_COUNT_AS_NUMBER_FROM_POSTS);
            postNumber = rs.next() ? rs.getInt("number") : 0;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.close();
        }
        return postNumber;
    }






}
