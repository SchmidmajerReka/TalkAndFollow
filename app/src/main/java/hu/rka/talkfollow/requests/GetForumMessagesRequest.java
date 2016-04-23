package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.ForumMessageResult;

/**
 * Created by Réka on 2016.04.18..
 */
public class GetForumMessagesRequest extends RetrofitSpiceRequest<ForumMessageResult, ContentInterface> {

    int molyid;

    public GetForumMessagesRequest(int molyid) {
        super(ForumMessageResult.class, ContentInterface.class);
        this.molyid = molyid;
    }

    @Override
    public ForumMessageResult loadDataFromNetwork() throws Exception {
        return getService().getForumMessages(molyid);
    }

}
