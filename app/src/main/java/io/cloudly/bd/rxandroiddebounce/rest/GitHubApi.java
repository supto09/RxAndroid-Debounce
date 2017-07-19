package io.cloudly.bd.rxandroiddebounce.rest;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by supto on 7/19/17.
 */

public class GitHubApi {

    public static final String TAG = GitHubApi.class.getSimpleName();

    private static GitHubApi instance;
    private GitHubApiServiceClient gitHubApiServiceClient;

    private GitHubApi() {

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(okHttpClient.build())
                .build();

        gitHubApiServiceClient = retrofit.create(GitHubApiServiceClient.class);

    }

    public static GitHubApi getInstance() {
        if (instance == null) {
            instance = new GitHubApi();
        }

        return instance;
    }

    public GitHubApiServiceClient getGitHubApiServiceClient() {
        return gitHubApiServiceClient;
    }
}
