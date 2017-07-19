package io.cloudly.bd.rxandroiddebounce.search;

import io.cloudly.bd.rxandroiddebounce.BasePresenter;
import io.cloudly.bd.rxandroiddebounce.BaseView;
import io.cloudly.bd.rxandroiddebounce.rest.data.GitHubUser;
import io.reactivex.annotations.NonNull;

/**
 * Created by supto on 7/19/17.
 */

public interface SearchContract {

    interface Presenter extends BasePresenter {
        void loadUserDetail(@NonNull String userName);
    }

    interface View extends BaseView<Presenter> {
        void showUserDetails(@NonNull GitHubUser gitHubUser);

        void displayToast(@NonNull String message);
    }
}
