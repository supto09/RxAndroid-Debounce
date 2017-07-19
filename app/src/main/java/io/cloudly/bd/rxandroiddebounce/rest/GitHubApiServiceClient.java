package io.cloudly.bd.rxandroiddebounce.rest;


import io.cloudly.bd.rxandroiddebounce.rest.data.GitHubUser;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by supto on 7/19/17.
 */

public interface GitHubApiServiceClient {

    @GET("/users/{username}")
    Observable<GitHubUser> getGitHubUser(@Path("username") String userName);

}
