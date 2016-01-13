package hu.rka.talkfollow.models;

import java.util.ArrayList;

/**
 * Created by Réka on 2016.01.13..
 */
public class VolumeInfo {

    String title;
    String subtitle;
    ArrayList<String> authors;
    String description;
    ArrayList<IndustryIdentifiers> industryIdentifiers;
    int pageCount = 0;
    ArrayList<String> categories;
    float averageRating = 0;
    ImageLinks imageLinks;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIndustryIdentifierses() {
        if(industryIdentifiers!=null) {
            if (industryIdentifiers.size() >= 1) {
                if (industryIdentifiers.get(0).getType() == "ISBN_13") {
                    return industryIdentifiers.get(0).getIdentifier();
                } else {
                    return industryIdentifiers.get(1).getIdentifier();
                }
            } else {
                return industryIdentifiers.get(0).getIdentifier();
            }
        }else{
            return "";
        }
    }

    public void setIndustryIdentifierses(ArrayList<IndustryIdentifiers> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }

    public String getDescription() {
        if(description!=null) {
            return description;
        }else{
        return "No Description";
        }
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

    public String getCategories() {
        if(categories!=null){
            String categoriesAll = categories.get(0);
            if(categories.size()>1){
                for (int i=1; i<categories.size()-1; i++) {
                    categoriesAll = categoriesAll + ", " + categories.get(i);
                }
                categoriesAll = categoriesAll + ", " + categories.get(categories.size());
            }
            return categoriesAll;
        }else{
            return "";
        }

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

    public String getImageLinks() {
        if(imageLinks!=null) {
            if (imageLinks.getExtraLarge() != null) {
                return imageLinks.getExtraLarge();
            } else if (imageLinks.getLarge() != null) {
                return imageLinks.getLarge();
            } else if (imageLinks.getMedium() != null) {
                return imageLinks.getMedium();
            } else if (imageLinks.getSmall() != null) {
                return imageLinks.getSmall();
            } else if (imageLinks.getThumbnail() != null) {
                return imageLinks.getThumbnail();
            } else {
                return imageLinks.getSmallThumbnail();
            }
        }else{
            return null;
        }
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public ArrayList<IndustryIdentifiers> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(ArrayList<IndustryIdentifiers> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }
}