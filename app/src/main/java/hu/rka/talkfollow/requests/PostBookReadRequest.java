package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadBookRead;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.EditBookmarkResult;
import hu.rka.talkfollow.results.SetBookReadResult;

/**
 * Created by Réka on 2016.01.25..
 */
public class PostBookReadRequest extends RetrofitSpiceRequest<SetBookReadResult, ContentInterface> {
private UploadBookRead uploadBookRead;

public PostBookReadRequest(UploadBookRead uploadBookRead) {
        super(SetBookReadResult.class, ContentInterface.class);
        this.uploadBookRead = uploadBookRead;
        }

@Override
public SetBookReadResult loadDataFromNetwork() {
        return getService().setBookRead(uploadBookRead);
        }
}
