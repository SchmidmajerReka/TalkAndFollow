package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.26..
 */
public class UploadRating {

    int book_id;
    float raiting;

    public UploadRating(int book_id, float raiting) {
        this.book_id = book_id;
        this.raiting = raiting;
    }
}
