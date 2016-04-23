package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.EditCriticResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostEditCriticRequest extends RetrofitSpiceRequest<EditCriticResult, ContentInterface> {

    UploadEditCritic uploadEditCritic;

    public PostEditCriticRequest(UploadEditCritic uploadEditCritic) {
        super(EditCriticResult.class, ContentInterface.class);
        this.uploadEditCritic = uploadEditCritic;
    }

    @Override
    public EditCriticResult loadDataFromNetwork() throws Exception {
        return getService().editCritic(uploadEditCritic);
    }
}
