package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.BestSellerResult;

/**
 * Created by Réka on 2016.01.22..
 */
public class GetBestSellersRequest extends RetrofitSpiceRequest<BestSellerResult, ContentInterface> {

    public GetBestSellersRequest() {
        super(BestSellerResult.class, ContentInterface.class);
    }

    @Override
    public BestSellerResult loadDataFromNetwork() throws Exception {
        return getService().getBestSellers();
    }
}