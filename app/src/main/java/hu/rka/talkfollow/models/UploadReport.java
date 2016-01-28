package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.27..
 */
public class UploadReport {

    int message_id;
    int moly_id;
    String message;

    public UploadReport(int message_id, int moly_id, String message) {
        this.message_id = message_id;
        this.moly_id = moly_id;
        this.message = message;
    }
}
