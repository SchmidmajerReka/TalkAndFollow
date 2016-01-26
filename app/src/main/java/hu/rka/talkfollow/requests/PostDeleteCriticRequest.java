package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadDeleteCritic;
import hu.rka.talkfollow.models.UploadEditCritic;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.DeleteCriticResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostDeleteCriticRequest extends RetrofitSpiceRequest<DeleteCriticResult, ContentInterface> {

    UploadDeleteCritic uploadDeleteCritic;

    public PostDeleteCriticRequest(UploadDeleteCritic uploadDeleteCritic) {
        super(DeleteCriticResult.class, ContentInterface.class);
        this.uploadDeleteCritic = uploadDeleteCritic;
    }

    @Override
    public DeleteCriticResult loadDataFromNetwork() throws Exception {
        return getService().deleteCritic(uploadDeleteCritic);
    }
}
