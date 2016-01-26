package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadBookAdd;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.SetBookAddedResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostBookAddRequest extends RetrofitSpiceRequest<SetBookAddedResult, ContentInterface> {
    private UploadBookAdd uploadBookAdd;

    public PostBookAddRequest(UploadBookAdd uploadBookAdd) {
        super(SetBookAddedResult.class, ContentInterface.class);
        this.uploadBookAdd = uploadBookAdd;
    }

    @Override
    public SetBookAddedResult loadDataFromNetwork() throws Exception {
        return getService().setBookAdded(uploadBookAdd);
    }
}
