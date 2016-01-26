package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.25..
 */
public class UploadVisibility {
    int book_id;
    boolean visibility;

    public UploadVisibility(int book_id, boolean visibility){
        this.book_id = book_id;
        this.visibility = visibility;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
