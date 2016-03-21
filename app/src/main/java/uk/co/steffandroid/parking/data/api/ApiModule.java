package uk.co.steffandroid.parking.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.urbanthings.gson.UrbanThingsGson;
import io.urbanthings.helpers.DateHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    private static final String URBAN_THINGS_URL = "https://bristol.api.urbanthings.io/api/1.0/";
    private static final String URBAN_THINGS_API_KEY = "";

    @Provides
    @Singleton
    UrbanThingsService provideUrbanThingsService(Retrofit retrofit) {
        return retrofit.create(UrbanThingsService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(URBAN_THINGS_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor interceptor, HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Interceptor provideInterceptor() {
        return chain -> {
            Request originalRequest = chain.request();

            Request request = originalRequest.newBuilder()
                    .url(originalRequest.url().newBuilder()
                            .addQueryParameter("apikey", URBAN_THINGS_API_KEY)
                            .build())
                    .build();

            return chain.proceed(request);
        };
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat(DateHelper.FAT_UTC_DATE_TIME_FORMAT)
                .registerTypeAdapterFactory(UrbanThingsGson.getPlacePointAdapterFactory())
                .create();
    }
}
