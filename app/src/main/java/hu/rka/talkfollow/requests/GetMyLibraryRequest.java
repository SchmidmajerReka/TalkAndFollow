package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.MyLibraryResult;

/**
 * Created by Réka on 2016.01.13..
 */
public class GetMyLibraryRequest extends RetrofitSpiceRequest<MyLibraryResult, ContentInterface> {

    public GetMyLibraryRequest() {
        super(MyLibraryResult.class, ContentInterface.class);
    }

    @Override
    public MyLibraryResult loadDataFromNetwork() throws Exception {
        return getService().getMyLibrary();
    }
}
