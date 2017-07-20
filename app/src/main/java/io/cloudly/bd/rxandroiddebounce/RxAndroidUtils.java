package io.cloudly.bd.rxandroiddebounce;

import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by supto on 7/20/17.
 */

public class RxAndroidUtils {


    private static final String TAG = RxAndroidUtils.class.getSimpleName();

    private static RxAndroidUtils instance;

    private RxAndroidUtils() {

    }

    public static RxAndroidUtils getInstance() {
        if (instance == null) {
            instance = new RxAndroidUtils();
        }

        return instance;
    }


    public Observable<String> getEditTextObservable(@NonNull EditText editText) {
        // method that takes a editText as input and returns an observable

        return RxTextView.textChanges(editText)

                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                });
    }
}
