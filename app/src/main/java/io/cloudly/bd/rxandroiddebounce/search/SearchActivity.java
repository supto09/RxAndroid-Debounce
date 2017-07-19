package io.cloudly.bd.rxandroiddebounce.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cloudly.bd.rxandroiddebounce.R;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);


        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getSimpleName());

        if(searchFragment ==  null){
            searchFragment = SearchFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(fragmentContainer.getId(),searchFragment,SearchFragment.class.getSimpleName()).commit();
    }
}
