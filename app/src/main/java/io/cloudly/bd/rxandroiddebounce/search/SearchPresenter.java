package io.cloudly.bd.rxandroiddebounce.search;

import io.cloudly.bd.rxandroiddebounce.rest.GitHubApi;
import io.cloudly.bd.rxandroiddebounce.rest.data.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by supto on 7/19/17.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchContract.View view;
    private Disposable gitHubUserObservableDisposable;

    private SearchPresenter(SearchContract.View view) {
        this.view = view;
    }

    public static SearchPresenter newInstance(SearchContract.View view) {
        SearchPresenter searchPresenter = new SearchPresenter(view);
        return searchPresenter;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

        // dispose all the observers
        if (gitHubUserObservableDisposable != null && !gitHubUserObservableDisposable.isDisposed()) {
            gitHubUserObservableDisposable.dispose();
        }
    }

    @Override
    public void loadUserDetail(@NonNull String userName) {

        // request over rest

        Observable<GitHubUser> gitHubUserObservable = GitHubApi.getInstance().getGitHubApiServiceClient().getGitHubUser(userName);

        gitHubUserObservableDisposable = gitHubUserObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GitHubUser>() {
                    @Override
                    public void accept(@NonNull GitHubUser gitHubUser) throws Exception {
                        view.displayToast(gitHubUser.getHtmlUrl());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.displayToast(throwable.getMessage());
                    }
                });
    }
}
