package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadBookmark;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.EditBookmarkResult;

/**
 * Created by Réka on 2016.01.25..
 */
public class PostBookmarkRequest extends RetrofitSpiceRequest<EditBookmarkResult, ContentInterface> {
    private UploadBookmark uploadBookmark;

    public PostBookmarkRequest(UploadBookmark uploadBookmark) {
        super(EditBookmarkResult.class, ContentInterface.class);
        this.uploadBookmark = uploadBookmark;
    }

    @Override
    public EditBookmarkResult loadDataFromNetwork() {
        return getService().editBookmark(uploadBookmark);
    }
}