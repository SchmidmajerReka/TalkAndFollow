package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadNewCritic;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.NewCriticResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostNewCriticRequest extends RetrofitSpiceRequest<NewCriticResult, ContentInterface> {

    private UploadNewCritic uploadNewCritic;

    public PostNewCriticRequest(UploadNewCritic uploadNewCritic) {
        super(NewCriticResult.class, ContentInterface.class);
        this.uploadNewCritic = uploadNewCritic;
    }

    @Override
    public NewCriticResult loadDataFromNetwork() throws Exception {
        return getService().newCritic(uploadNewCritic);
    }
}
