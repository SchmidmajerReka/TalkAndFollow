package hu.rka.talkfollow.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Réka on 2016.01.08..
 */
public class Book {
    int id;
    int moly_id;
    String title;
    String subtitle;
    String picture;
    ArrayList<String> authors;
    ArrayList<String> tags;
    boolean finished;
    float average_rating;
    float my_rating;
    String descripton;
    int bookmark;
    boolean mine;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMolyid() {
        return moly_id;
    }

    public void setMolyid(int molyid) {
        this.moly_id = molyid;
    }

    public String getTitle() {
        if(subtitle!=null){
            return title + ": " + subtitle;
        }else{
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAuthors() {
        if(authors!=null) {
            String authorInfo = authors.get(0);
            if (authors.size() > 1) {
                for (int i = 1; i < authors.size() - 1; i++) {
                    authorInfo = authorInfo + ", " + authors.get(i);
                }
                authorInfo = authorInfo + ", " + authors.get(authors.size() - 1);
            }
            return authorInfo;
        }else{
            return "Author Unknown";
        }
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getTags() {
        if(tags!=null){
            String allTags = tags.get(0);
            if(tags.size()>1){
                for (int i=1; i<tags.size()-1; i++) {
                    allTags = allTags + ", " + tags.get(i);
                }
                allTags = allTags + ", " + tags.get(tags.size()-1);
            }
            return allTags;
        }else{
            return "";
        }
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public float getMy_rating() {
        return my_rating;
    }

    public void setMy_rating(float my_rating) {
        this.my_rating = my_rating;
    }

    public String getDescription() {
        return descripton;
    }

    public void setDescription(String description) {
        this.descripton = description;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }
}
