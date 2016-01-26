package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadRating;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.EditRatingResult;

/**
 * Created by Réka on 2016.01.26..
 */
public class PostRatingRequest extends RetrofitSpiceRequest<EditRatingResult, ContentInterface> {

    private UploadRating uploadRating;

    public PostRatingRequest(UploadRating uploadRating) {
        super(EditRatingResult.class, ContentInterface.class);
        this.uploadRating = uploadRating;
    }

    @Override
    public EditRatingResult loadDataFromNetwork() throws Exception {
        return getService().editRating(uploadRating);
    }
}
