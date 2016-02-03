package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.SetBookAddedResult;

/**
 * Created by User on 03/02/16.
 */
public class GetAddBookRequest extends RetrofitSpiceRequest <SetBookAddedResult, ContentInterface> {

    int moly_id;

    public GetAddBookRequest(int moly_id) {
        super(SetBookAddedResult.class, ContentInterface.class);
        this.moly_id = moly_id;
    }

    @Override
    public SetBookAddedResult loadDataFromNetwork() throws Exception {
        return getService().getAddBook(moly_id);
    }
}
