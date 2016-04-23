package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.28..
 */
public class UploadUser {

    String facebook_id;
    String first_name;
    String last_name;

    public UploadUser(String facebook_id, String first_name, String last_name) {
        this.facebook_id = facebook_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
