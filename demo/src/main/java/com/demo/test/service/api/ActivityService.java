package com.demo.test.service.api;

import com.demo.test.entities.StartBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ActivityService {

    @GET("/rest/activitys")
    Observable<StartBean> getStartAds();
}
