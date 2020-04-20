package entities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    private String username;
    private String text;
    private String date;
    private DateFormat dateFormat = new SimpleDateFormat("MMM d, h:mm a", Locale.ENGLISH);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Timestamp ts) {
        Date newD=new Date(ts.getTime());
        this.date=dateFormat.format(newD);
    }

}
