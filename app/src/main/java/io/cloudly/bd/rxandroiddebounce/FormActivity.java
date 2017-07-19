package io.cloudly.bd.rxandroiddebounce;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = FormActivity.class.getSimpleName();

    @BindView(R.id.user_name_edit_text)
    EditText userNameEt;

    @BindView(R.id.email_edit_text)
    EditText emailEt;

    @BindView(R.id.password_edit_text)
    EditText passwordEt;

    @BindView(R.id.sign_up_button)
    Button signUpButton;

    private Disposable combinedDisposable;
    private Disposable userNameDisposable;
    private Disposable emailDisposable;
    private Disposable passwordDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);


        // create observables for editTexts
        Observable<String> userNameObservable = getEditTextObservable(userNameEt);
        Observable<String> emailObservable = getEditTextObservable(emailEt);
        Observable<String> passwordObservable = getEditTextObservable(passwordEt);


        // use each observable for validation
        userNameDisposable = userNameObservable
                .debounce(800, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String userName) throws Exception {
                        if (userName.length() < 3) {

                            userNameEt.setError("Username too short");
                        }
                    }
                });

        // use each observable for validation
        emailDisposable = emailObservable
                .debounce(800, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String email) throws Exception {
                        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                            emailEt.setError("Not a valid email");
                        }
                    }
                });

        // use each observable for validation
        passwordDisposable = passwordObservable
                .debounce(800, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String password) throws Exception {
                        if (password.length() < 4) {
                            passwordEt.setError("Password too short");
                        }
                    }
                });


        // create a combined observable that does the form validation and returns a boolean result
        Observable<Boolean> combinedObservable = Observable.combineLatest(userNameObservable, emailObservable, passwordObservable, new Function3<String, String, String, Boolean>() {
            @Override
            public Boolean apply(@NonNull String userName, @NonNull String email, @NonNull String password) throws Exception {

                boolean userNameCondition = true;
                boolean emailCondition = true;
                boolean passwordCondition = true;

                if (userName.length() < 3) {
                    userNameCondition = false;
                }
                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    emailCondition = false;
                }

                if (password.length() < 4) {
                    passwordCondition = false;
                }
                return (userNameCondition && emailCondition && passwordCondition);
            }
        });

        // observe the combined observable to activate the submit button
        combinedDisposable = combinedObservable.observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull final Boolean aBoolean) throws Exception {

                        signUpButton.setClickable(aBoolean);
                        if (aBoolean)
                            signUpButton.setText("Active");
                        else
                            signUpButton.setText("Not Active");

                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // dispose all the disposable subscription

        if (userNameDisposable != null && !userNameDisposable.isDisposed()) {
            userNameDisposable.dispose();
        }
        if (passwordDisposable != null && !passwordDisposable.isDisposed()) {
            passwordDisposable.dispose();
        }
        if (emailDisposable != null && !emailDisposable.isDisposed()) {
            emailDisposable.dispose();
        }
        if (combinedDisposable != null && !combinedDisposable.isDisposed()) {
            combinedDisposable.dispose();
        }
    }

    private Observable<String> getEditTextObservable(EditText editText) {

        // method that takes a editText as input and returns an observable

        return RxTextView.textChanges(editText)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(@NonNull CharSequence charSequence) throws Exception {
                        return charSequence.length() > 0;
                    }
                })
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {

                        return charSequence.toString();
                    }
                });
    }
}
