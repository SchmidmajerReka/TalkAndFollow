package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadVisibility;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.SetVisibilityResult;

/**
 * Created by Réka on 2016.01.25..
 */
public class PostVisibilityRequest extends RetrofitSpiceRequest<SetVisibilityResult, ContentInterface> {
    private UploadVisibility uploadVisibility;

    public PostVisibilityRequest(UploadVisibility uploadVisibility) {
        super(SetVisibilityResult.class, ContentInterface.class);
        this.uploadVisibility = uploadVisibility;
    }

    @Override
    public SetVisibilityResult loadDataFromNetwork() {
        return getService().setVisibility(uploadVisibility);
    }
}