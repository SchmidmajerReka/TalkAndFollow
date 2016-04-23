package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.RecommendationsResult;

/**
 * Created by Réka on 2016.01.22..
 */
public class GetRecommendationsRequest extends RetrofitSpiceRequest<RecommendationsResult, ContentInterface> {

    public GetRecommendationsRequest() {
        super(RecommendationsResult.class, ContentInterface.class);
    }

    @Override
    public RecommendationsResult loadDataFromNetwork() throws Exception {
        return getService().getBestSellers();
    }
}