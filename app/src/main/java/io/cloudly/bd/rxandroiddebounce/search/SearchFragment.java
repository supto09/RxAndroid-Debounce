package io.cloudly.bd.rxandroiddebounce.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cloudly.bd.rxandroiddebounce.R;
import io.cloudly.bd.rxandroiddebounce.RxAndroidUtils;
import io.cloudly.bd.rxandroiddebounce.rest.data.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


public class SearchFragment extends Fragment implements SearchContract.View {

    private static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @BindView(R.id.search_text_view)
    TextView searchTextView;

    private SearchContract.Presenter presenter;
    private Disposable searchEditTextObservableDisposable;

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);


        // get a observable from searchEditText
        Observable<String> searchEditTextObservable = RxAndroidUtils.getInstance().getEditTextObservable(searchEditText);

        // make a request from text input with a debounce
        searchEditTextObservableDisposable = searchEditTextObservable
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.length() > 3;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        presenter.loadUserDetail(s);
                    }
                });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = SearchPresenter.newInstance(this);
            presenter.start();
        }
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();

        // end the presenter to dispose all the observers
        presenter.end();

        // dispose all the observers
        if (searchEditTextObservableDisposable != null && !searchEditTextObservableDisposable.isDisposed()) {
            searchEditTextObservableDisposable.dispose();
        }
    }

    @Override
    public void showUserDetails(@NonNull GitHubUser gitHubUser) {
        Toast.makeText(getActivity(), gitHubUser.getHtmlUrl(), Toast.LENGTH_SHORT).show();
        Log.d(TAG,"---------output: "+ gitHubUser.getHtmlUrl());
    }

    @Override
    public void displayToast(@NonNull String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG,"---------output: "+ message);

    }
}
