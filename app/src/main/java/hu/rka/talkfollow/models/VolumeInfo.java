package hu.rka.talkfollow.models;

import java.util.ArrayList;

/**
 * Created by Réka on 2016.01.13..
 */
public class VolumeInfo {

    String title;
    String subtitle="";
    ArrayList<String> authors;
    String description;
    ArrayList<IndustryIdentifiers> industryIdentifierses;
    int pageCount;
    ArrayList<String> categories;
    float averageRating;
    String medium;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        String authorInfo = authors.get(0);
        if(authors.size()>1){
            for(int i = 1; i<authors.size()-1; i++){
                authorInfo=authorInfo + ", " + authors.get(i);
            }
            authorInfo = authorInfo + ", " + authors.get(authors.size()-1);
        }
        return authorInfo;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIndustryIdentifierses() {
        if(industryIdentifierses.size()>1){
            if(industryIdentifierses.get(0).getType() == "ISBN_13"){
                return industryIdentifierses.get(0).getIdentifier();
            }else{
                return industryIdentifierses.get(1).getIdentifier();
            }
        }else{
            return industryIdentifierses.get(0).getIdentifier();
        }
    }

    public void setIndustryIdentifierses(ArrayList<IndustryIdentifiers> industryIdentifierses) {
        this.industryIdentifierses = industryIdentifierses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}