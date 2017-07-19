package io.cloudly.bd.rxandroiddebounce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cloudly.bd.rxandroiddebounce.search.SearchActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SingleEditTextWithDebounce extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SingleEditTextWithDebounce.class.getSimpleName();

    @BindView(R.id.edit_text)
    EditText editText;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.search_button)
    Button searchButton;

    @BindView(R.id.form_button)
    Button formButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_edit_text_debounce);

        ButterKnife.bind(this);

        searchButton.setOnClickListener(this);
        formButton.setOnClickListener(this);

        RxTextView.textChanges(editText)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(@NonNull CharSequence charSequence) throws Exception {

                        // filtering the input

                        return charSequence.length() > 2;
                    }
                })
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {

                        // mapping the input to convert into strings

                        return charSequence.toString();
                    }
                })
                .debounce(800, TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull final String s) throws Exception {
                                //warn if not a valid email address

                                if (s.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                                    textView.setText(s);
                                else
                                    editText.setError("Not a valid email");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == formButton) {
            startActivity(new Intent(SingleEditTextWithDebounce.this, FormActivity.class));
        } else if (view == searchButton) {
            startActivity(new Intent(SingleEditTextWithDebounce.this, SearchActivity.class));
        }
    }
}
