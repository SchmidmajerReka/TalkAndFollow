package hu.rka.talkfollow.network;

import android.content.Context;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.RestAdapter;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;

/**
 * Created by Réka on 2016.01.13..
 */
public class ContentSpiceService  extends RetrofitGsonSpiceService {

    private static Context context;

    private static class TimeoutUrlConnection extends UrlConnectionClient {
        @Override
        protected HttpURLConnection openConnection(Request request) throws IOException {
            HttpURLConnection connection = super.openConnection(request);
            connection.setConnectTimeout(8000);
            return connection;
        }
    }

    public static RestAdapter.Builder getRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setEndpoint("https://www.googleapis.com")
                .setClient(new TimeoutUrlConnection());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        addRetrofitInterface(ContentInterface.class);
    }



    @Override
    protected String getServerUrl() {
        return "https://www.googleapis.com";
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return getRestAdapterBuilder();
    }

}
