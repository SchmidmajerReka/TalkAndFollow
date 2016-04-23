package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.27..
 */
public class UploadVote {

    int message_id;
    int moly_id;
    String direction; //up vagy down


    public UploadVote(int message_id, int moly_id, String direction) {
        this.message_id = message_id;
        this.moly_id = moly_id;
        this.direction = direction;
    }
}
