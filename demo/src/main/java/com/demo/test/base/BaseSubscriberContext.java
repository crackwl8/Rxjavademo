package com.demo.test.base;

import io.reactivex.disposables.Disposable;


public interface BaseSubscriberContext {
    void addDisposable(Disposable d);
}
