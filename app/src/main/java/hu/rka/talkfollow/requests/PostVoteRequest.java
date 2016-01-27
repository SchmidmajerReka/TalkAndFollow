package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadVote;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.VoteResult;

/**
 * Created by Réka on 2016.01.27..
 */
public class PostVoteRequest extends RetrofitSpiceRequest<VoteResult, ContentInterface> {

    private UploadVote uploadVote;

    public PostVoteRequest(UploadVote uploadVote) {
        super(VoteResult.class, ContentInterface.class);
        this.uploadVote = uploadVote;
    }

    @Override
    public VoteResult loadDataFromNetwork() throws Exception {
        return getService().vote(uploadVote);
    }
}
