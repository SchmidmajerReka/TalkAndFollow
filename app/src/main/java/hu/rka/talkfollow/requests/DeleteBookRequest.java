package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.DeleteBookResult;

/**
 * Created by Réka on 2016.04.09..
 */
public class DeleteBookRequest extends RetrofitSpiceRequest<DeleteBookResult, ContentInterface> {

    int book_id;

    public DeleteBookRequest(int book_id) {
        super(DeleteBookResult.class, ContentInterface.class);
        this.book_id = book_id;
    }

    @Override
    public DeleteBookResult loadDataFromNetwork() throws Exception {
        return getService().deleteBook(book_id);
    }
}
