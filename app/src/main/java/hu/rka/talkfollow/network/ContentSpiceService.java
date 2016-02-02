package hu.rka.talkfollow.network;

import android.content.Context;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import java.io.IOException;
import java.net.HttpURLConnection;

import hu.rka.talkfollow.PreferencesHelper;
import retrofit.RequestInterceptor;
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
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                String token = PreferencesHelper.getStringByKey(context, "Auth-Token", "");
                if(token != null && !token.equalsIgnoreCase("")){
                    request.addHeader("Auth-Token", token);
                }
            }
        };


        return new RestAdapter.Builder()
                .setEndpoint("http://talkandfollow.konstruktor.hu/api/v1")
                .setClient(new TimeoutUrlConnection())
                .setRequestInterceptor(requestInterceptor);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        addRetrofitInterface(ContentInterface.class);
    }

    @Override
    protected String getServerUrl() {
        return "http://emih.konstruktor.hu/";
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return getRestAdapterBuilder();
    }

}
