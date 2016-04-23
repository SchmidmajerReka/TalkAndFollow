package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.26..
 */
public class UploadNewCritic {

    int book_id;
    String title;
    String critic;
    float rating;

    public UploadNewCritic(int book_id, String title, String critic, float rating) {
        this.book_id = book_id;
        this.title = title;
        this.critic = critic;
        this.rating = rating;
    }


}
