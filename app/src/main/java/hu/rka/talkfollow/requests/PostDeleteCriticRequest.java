package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.DeleteCriticResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostDeleteCriticRequest extends RetrofitSpiceRequest<DeleteCriticResult, ContentInterface> {

    int critic_id;

    public PostDeleteCriticRequest(int critic_id) {
        super(DeleteCriticResult.class, ContentInterface.class);
        this.critic_id = critic_id;
    }

    @Override
    public DeleteCriticResult loadDataFromNetwork() throws Exception {
        return getService().deleteCritic(critic_id);
    }
}
