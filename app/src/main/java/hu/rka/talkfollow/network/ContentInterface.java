package hu.rka.talkfollow.network;

import hu.rka.talkfollow.models.Book;
import hu.rka.talkfollow.results.BookResults;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Callback;

/**
 * Created by Réka on 2016.01.13..
 */
public interface ContentInterface {
    @GET("{qpath}:{isbn}")
    Call<Book> getBookbyISBN(@Path("qpath") @Query("q") String requestBy, @Path("isbn") String isbn, @Query("key") String key);
}
//&key=AIzaSyDR0UjdaBWHcXBvBzG88Y4So4o9JeAVhr8")