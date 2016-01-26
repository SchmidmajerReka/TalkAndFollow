package hu.rka.talkfollow.network;

import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.results.BestSellerResult;
import hu.rka.talkfollow.results.DetailsResult;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.EditProfileResult;
import hu.rka.talkfollow.results.EditRatingResult;
import hu.rka.talkfollow.results.MyProfileResult;
import hu.rka.talkfollow.results.MyLibraryResult;
import hu.rka.talkfollow.results.SearchResult;
import hu.rka.talkfollow.results.SetBookAddedResult;
import hu.rka.talkfollow.results.SetBookReadResult;
import hu.rka.talkfollow.results.SetVisibilityResult;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Réka on 2016.01.13..
 */
public interface ContentInterface {
    @GET("/MyLibrary.json")
    MyLibraryResult getMyLibrary();

    @GET("/MyProfile.json")
    MyProfileResult getMyProfile();

    @GET("/{molyid}.json")
    DetailsResult getDetails(@Path("molyid") int molyid);

    @GET("/BestSellerList.json")
    BestSellerResult getBestSellers();

    @GET("/{search}.json")
    SearchResult getSearchResult(@Path("search") String expression);

    @POST("/edit_user")
    EditProfileResult editProfile(@Body UploadProfile uploadProfile);

    @POST("/book_page")
    EditBookmarkResult editBookmark(@Body UploadBookmark uploadBookmark);

    @POST("/book_read")
    SetBookReadResult setBookRead(@Body UploadBookRead uploadBookRead);

    @POST("/book_show_user")
    SetVisibilityResult setVisibility(@Body UploadVisibility uploadVisibility);

    //add_book
    @POST("/add_book")
    SetBookAddedResult setBookAdded(@Body UploadBookAdd uploadBookAdd);

    //rate_book
    @POST("/MyLibrary.json")
    EditRatingResult editRating(@Body UploadRating uploadRating);
}
//&key=AIzaSyDR0UjdaBWHcXBvBzG88Y4So4o9JeAVhr8")