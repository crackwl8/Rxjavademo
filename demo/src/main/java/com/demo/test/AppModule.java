package com.demo.test;

import com.demo.test.module.ActivityInteractor;
import com.demo.test.module.ActivityInteractorImpl;
import com.demo.test.service.RetrofitClient;
import com.demo.test.service.api.ActivityService;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module
public class AppModule {

    @Provides
    public Retrofit provideRestAdapter() {
        return RetrofitClient.createAdapter();
    }

    @Singleton
    @Provides
    public ActivityService provideActivityApi(Retrofit retrofit) {
        return retrofit.create(ActivityService.class);
    }

    @Singleton
    @Provides
    public ActivityInteractor provideActivityInteractor(ActivityService service) {
        return new ActivityInteractorImpl(service);
    }
}
