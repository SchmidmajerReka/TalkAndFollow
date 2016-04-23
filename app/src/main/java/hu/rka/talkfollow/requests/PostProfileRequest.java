package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadProfile;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.EditProfileResult;

/**
 * Created by Réka on 2016.01.25..
 */
public class PostProfileRequest extends RetrofitSpiceRequest<EditProfileResult, ContentInterface> {
    private UploadProfile uploadProfile;

    public PostProfileRequest(UploadProfile uploadProfile) {
        super(EditProfileResult.class, ContentInterface.class);
        this.uploadProfile = uploadProfile;
    }

    @Override
    public EditProfileResult loadDataFromNetwork() {
        return getService().editProfile(uploadProfile);
    }
}

