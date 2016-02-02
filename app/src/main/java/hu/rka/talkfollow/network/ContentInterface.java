package hu.rka.talkfollow.network;


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

    //report_forum_message
    @POST("/MyLibrary.json")
    ReportResult report(@Body UploadReport uploadReport);

    //write_forum_message
    @POST("/MyLibrary.json")
    ForumMessageResult writeMessage(@Body UploadForumMessage uploadForumMessage);

    //login
    @POST("/MyLibrary.json")
    UserResult login(@Body UploadUser uploadUser);

}