package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.MyProfileResult;

/**
 * Created by Réka on 2016.01.21..
 */
public class GetMyProfileRequest extends RetrofitSpiceRequest<MyProfileResult, ContentInterface> {

    public GetMyProfileRequest() {
        super(MyProfileResult.class, ContentInterface.class);
    }

    @Override
    public MyProfileResult loadDataFromNetwork() throws Exception {
        return getService().getMyProfile();
    }


}


