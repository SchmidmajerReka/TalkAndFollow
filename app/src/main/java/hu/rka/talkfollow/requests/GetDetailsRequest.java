package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.DetailsResult;

/**
 * Created by Réka on 2016.01.21..
 */
public class GetDetailsRequest extends RetrofitSpiceRequest<DetailsResult, ContentInterface> {

    int molyid;

    public GetDetailsRequest(int molyid) {
        super(DetailsResult.class, ContentInterface.class);
        this.molyid = molyid;
    }

    @Override
    public DetailsResult loadDataFromNetwork() throws Exception {
        return getService().getDetails(molyid);
    }

}
