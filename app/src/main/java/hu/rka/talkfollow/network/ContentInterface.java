package hu.rka.talkfollow.network;

import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.models.UploadDeleteCritic;
import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.models.UploadNewCritic;
import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.models.UploadVote;
import hu.rka.talkfollow.results.BestSellerResult;
import hu.rka.talkfollow.results.DeleteCriticResult;
import hu.rka.talkfollow.results.DetailsResult;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.EditCriticResult;
import hu.rka.talkfollow.results.EditProfileResult;
import hu.rka.talkfollow.results.EditRatingResult;
import hu.rka.talkfollow.results.MyProfileResult;
import hu.rka.talkfollow.results.MyLibraryResult;
import hu.rka.talkfollow.results.NewCriticResult;
import hu.rka.talkfollow.results.SearchResult;
import hu.rka.talkfollow.results.SetBookAddedResult;
import hu.rka.talkfollow.results.SetBookReadResult;
import hu.rka.talkfollow.results.SetVisibilityResult;
import hu.rka.talkfollow.results.VoteResult;
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

    //edit_user
    @POST("/MyLibrary.json")
    EditProfileResult editProfile(@Body UploadProfile uploadProfile);

    //book_page
    @POST("/MyLibrary.json")
    EditBookmarkResult editBookmark(@Body UploadBookmark uploadBookmark);

    //book_read
    @POST("/MyLibrary.json")
    SetBookReadResult setBookRead(@Body UploadBookRead uploadBookRead);

    //book_show_user
    @POST("/MyLibrary.json")
    SetVisibilityResult setVisibility(@Body UploadVisibility uploadVisibility);

    //add_book
    @POST("/MyLibrary.json")
    SetBookAddedResult setBookAdded(@Body UploadBookAdd uploadBookAdd);

    //rate_book
    @POST("/MyLibrary.json")
    EditRatingResult editRating(@Body UploadRating uploadRating);

    //critic
    @POST("/MyLibrary.json")
    NewCriticResult newCritic(@Body UploadNewCritic uploadNewCritic);

    //critic
    @POST("/MyLibrary.json")
    EditCriticResult editCritic(@Body UploadEditCritic uploadEditCritic);

    //delete_critic
    @POST("/MyLibrary.json")
    DeleteCriticResult deleteCritic(@Body UploadDeleteCritic uploadDeleteCritic);

    //upvote/downvote
    @POST("/MyLibrary.json")
    VoteResult vote(@Body UploadVote uploadVote);

}
//&key=AIzaSyDR0UjdaBWHcXBvBzG88Y4So4o9JeAVhr8")