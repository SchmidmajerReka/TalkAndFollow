package hu.rka.talkfollow.results;

/**
 * Created by Réka on 2016.01.21..
 */
public class MyProfileResult {

    String picture;
    int books_number;
    int finished;
    String about_me;
    String msg;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getBooks_number() {
        return books_number;
    }

    public void setBooks_number(int books_number) {
        this.books_number = books_number;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getMsg() {
        return msg;
    }
}
