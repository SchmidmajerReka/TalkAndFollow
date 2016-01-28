package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.27..
 */
public class UploadForumMessage {

    int moly_id;
    String message;

    public UploadForumMessage(int moly_id, String message) {
        this.moly_id = moly_id;
        this.message = message;
    }
}
