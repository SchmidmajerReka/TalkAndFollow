package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.09..
 */
public class Readers {
    int id;
    String name;
    long book_added;
    String user_picture;
    float rating;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBook_added() {
        return book_added;
    }

    public void setBook_added(long book_added) {
        this.book_added = book_added;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
