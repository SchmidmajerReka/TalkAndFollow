package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.SearchResult;

/**
 * Created by Réka on 2016.01.25..
 */
public class GetSearchResultRequest extends RetrofitSpiceRequest<SearchResult, ContentInterface> {

    String expression;

    public GetSearchResultRequest(String expression) {
        super(SearchResult.class, ContentInterface.class);
        this.expression = expression;
    }

    @Override
    public SearchResult loadDataFromNetwork() throws Exception {
        return getService().getSearchResult(expression);
    }

}
