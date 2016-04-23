package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.10..
 */
public class ForumMessage {

    int id;
    String user_name; //name
    String message;
    String user_picture;
    int approval_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public int getApproval_count() {
        return approval_count;
    }

    public void setApproval_count(int approval_count) {
        this.approval_count = approval_count;
    }
}
