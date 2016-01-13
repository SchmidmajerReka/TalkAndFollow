package hu.rka.talkfollow.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Réka on 2016.01.08..
 */
public class Book {
    String id;
    VolumeInfo volumeInfo;
    int pageRead;
    float myRating;


    public int getPageRead() {
        return pageRead;
    }

    public void setPageRead(int pageRead) {
        this.pageRead = pageRead;
    }


    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
