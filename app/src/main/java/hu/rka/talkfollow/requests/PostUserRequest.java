package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadUser;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.UserResult;

/**
 * Created by Réka on 2016.01.28..
 */
public class PostUserRequest extends RetrofitSpiceRequest<UserResult, ContentInterface> {

    UploadUser uploadUser;

    public PostUserRequest(UploadUser uploadUser) {
        super(UserResult.class, ContentInterface.class);
        this.uploadUser = uploadUser;
    }

    @Override
    public UserResult loadDataFromNetwork() throws Exception {
        return getService().login(uploadUser);
    }
}
