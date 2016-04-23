package hu.rka.talkfollow.network;

import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.models.UploadForumMessage;
import hu.rka.talkfollow.models.UploadNewCritic;
import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.models.UploadReport;
import hu.rka.talkfollow.models.UploadUser;
import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.models.UploadVote;
import hu.rka.talkfollow.results.RecommendationsResult;
import hu.rka.talkfollow.results.DeleteBookResult;
import hu.rka.talkfollow.results.DeleteCriticResult;
import hu.rka.talkfollow.results.DetailsResult;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.EditCriticResult;
import hu.rka.talkfollow.results.EditProfileResult;
import hu.rka.talkfollow.results.EditRatingResult;
import hu.rka.talkfollow.results.ForumMessageResult;
import hu.rka.talkfollow.results.MyLibraryResult;
import hu.rka.talkfollow.results.MyProfileResult;
import hu.rka.talkfollow.results.NewCriticResult;
import hu.rka.talkfollow.results.ReportResult;
import hu.rka.talkfollow.results.SearchResult;
import hu.rka.talkfollow.results.SetBookAddedResult;
import hu.rka.talkfollow.results.SetBookReadResult;
import hu.rka.talkfollow.results.SetVisibilityResult;
import hu.rka.talkfollow.results.UserResult;
import hu.rka.talkfollow.results.VoteResult;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Réka on 2016.01.13..
 */
public interface ContentInterface {

    //bejelentkezés
    @POST("/login")
    UserResult login(@Body UploadUser uploadUser);

    //saját könyvtár
    @GET("/mylibrary")
    MyLibraryResult getMyLibrary();

    //felhasználó profilja
    @GET("/users/profile")
    MyProfileResult getMyProfile();

    //Könyv részletek
    @GET("/details")
    DetailsResult getDetails(@Query("moly_id") int molyid);

    //új kritika
    @POST("/critic")
    NewCriticResult newCritic(@Body UploadNewCritic uploadNewCritic);

    //kritika szerkesztése
    @PUT("/critic")
    EditCriticResult editCritic(@Body UploadEditCritic uploadEditCritic);

    //kritika törlése
    @DELETE("/critic")
    DeleteCriticResult deleteCritic(@Query("critic_id") int id);

    //új fórum özenet
    @POST("/write_forum_message")
    ForumMessageResult writeMessage(@Body UploadForumMessage uploadForumMessage);

    //sértő tartalom jelentése
    @POST("/report_forum_message")
    ReportResult report(@Body UploadReport uploadReport);

    //Keresés
    @GET("/search")
    SearchResult getSearchResult(@Query("q") String expression);

    //profil szerkesztése
    @POST("/edit_user")
    EditProfileResult editProfile(@Body UploadProfile uploadProfile);

    //oldalszám szerkesztése
    @POST("/book_page")
    EditBookmarkResult editBookmark(@Body UploadBookmark uploadBookmark);

    //könyv értékelése
    @POST("/rate_book")
    EditRatingResult editRating(@Body UploadRating uploadRating);

    //megjelölés kiolvasottként
    @POST("/book_read")
    SetBookReadResult setBookRead(@Body UploadBookRead uploadBookRead);

    //olvasók listájában való láthatóság
    @POST("/book_show_user")
    SetVisibilityResult setVisibility(@Body UploadVisibility uploadVisibility);

    //könyv hozzáadása
    @POST("/add_book_to_my_library")
    SetBookAddedResult setBookAdded(@Body UploadBookAdd uploadBookAdd);

    //Könyv törlése a saját könyvtárból
    @DELETE("/delete_book_from_my_library")
    DeleteBookResult deleteBook(@Query("book_id") int book_id);

    //fórum üzenet értékelése
    @POST("/forum_message_rate")
    VoteResult vote(@Body UploadVote uploadVote);

    //ajánló
    @GET("/recommendations")
    RecommendationsResult getBestSellers();

    //fórum üzenetek frissítése
    @GET("/books/{book_id}/forum_messages")
    ForumMessageResult getForumMessages(@Path("book_id") int book_id);
}