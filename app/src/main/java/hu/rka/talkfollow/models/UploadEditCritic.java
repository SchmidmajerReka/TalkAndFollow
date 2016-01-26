package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.26..
 */
public class UploadEditCritic {
    int ritic_id;
    String title;
    String critic;
    float rating;

    public UploadEditCritic(int ritic_id, String title, String critic, float rating) {
        this.ritic_id = ritic_id;
        this.title = title;
        this.critic = critic;
        this.rating = rating;
    }
}
