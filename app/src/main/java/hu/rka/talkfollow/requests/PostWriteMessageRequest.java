package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadForumMessage;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.ForumMessageResult;

/**
 * Created by Réka on 2016.01.27..
 */
public class PostWriteMessageRequest extends RetrofitSpiceRequest<ForumMessageResult, ContentInterface> {

    private UploadForumMessage uploadForumMessage;

    public PostWriteMessageRequest( UploadForumMessage uploadForumMessage) {
        super(ForumMessageResult.class, ContentInterface.class);
        this.uploadForumMessage = uploadForumMessage;
    }

    @Override
    public ForumMessageResult loadDataFromNetwork() throws Exception {
        return getService().writeMessage(uploadForumMessage);
    }
}
