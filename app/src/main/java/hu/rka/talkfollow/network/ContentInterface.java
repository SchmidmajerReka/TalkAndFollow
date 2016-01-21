package hu.rka.talkfollow.network;

import hu.rka.talkfollow.results.DetailsResult;
import hu.rka.talkfollow.results.MyProfileResult;
import hu.rka.talkfollow.results.MyLibraryResult;
import retrofit.http.GET;
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
}
//&key=AIzaSyDR0UjdaBWHcXBvBzG88Y4So4o9JeAVhr8")