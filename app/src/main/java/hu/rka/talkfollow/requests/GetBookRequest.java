package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.BookResults;

/**
 * Created by Réka on 2016.01.13..
 */
public class GetBookRequest  extends RetrofitSpiceRequest<BookResults, ContentInterface> {
    String isbn;

    public GetBookRequest(String isbn) {
        super(BookResults.class, ContentInterface.class);
        this.isbn=isbn;
    }

    @Override
    public BookResults loadDataFromNetwork() throws Exception {
        return getService().getBookbyISBN("isbn:" + isbn, "AIzaSyDR0UjdaBWHcXBvBzG88Y4So4o9JeAVhr8");
    }
}
