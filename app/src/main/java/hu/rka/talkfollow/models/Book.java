package hu.rka.talkfollow.models;

/**
 * Created by Réka on 2016.01.08..
 */
public class Book {
    String id;
    String author;
    String title;
    String url;
    String isbn;
    String genre;
    int pageNum;
    int pageRead;
    float otherRating;
    float myRating;
    String description;



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageRead() {
        return pageRead;
    }

    public void setPageRead(int pageRead) {
        this.pageRead = pageRead;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public float getOtherRating() {
        return otherRating;
    }

    public void setOtherRating(float otherRating) {
        this.otherRating = otherRating;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
