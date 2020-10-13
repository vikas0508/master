package Utils;

import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

//import constants.ApiConstants;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
//import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
          /*  Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(1);
            dispatcher.cancelAll();*/
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
               //     .dispatcher(dispatcher)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60,TimeUnit.MINUTES)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
       /*     Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();*/

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.Base_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //get retrofit instance
/*
    public static Retrofit getRestServiceGoogle() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.GOOGLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }
*/


    //get OkHttp instance
  /*  public static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(httpLoggingInterceptor);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        return builder.build();
    }*/
    static class OkHttpClientExt extends OkHttpClient {
        static final Object TAG_CALL = new Object();

        @Override
        public Call newCall(Request request) {
            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.tag(TAG_CALL);
            return super.newCall(requestBuilder.build());
        }
    }


}
