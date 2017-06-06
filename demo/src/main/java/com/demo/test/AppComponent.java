package com.demo.test;



import com.demo.test.module.ActivityInteractor;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    ActivityInteractor getActivityInteractor();

}