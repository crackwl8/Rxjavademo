package com.demo.library.rx;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**

 */
public class DefaultTransform<T> implements ObservableTransformer<T, T> {
    private int count = 0;

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .retryWhen(throwableObservable ->
                throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (count++ <= 2) {
                        return Observable.timer(2000, TimeUnit.MILLISECONDS);
                    }
                    return Observable.error(throwable);
                }));
    }
}
