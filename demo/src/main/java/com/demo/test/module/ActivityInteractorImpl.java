package com.demo.test.module;

import com.demo.library.rx.DefaultTransform;
import com.demo.test.entities.StartBean;
import com.demo.test.service.ServiceResponse;
import com.demo.test.service.api.ActivityService;

import javax.inject.Inject;


public class ActivityInteractorImpl implements ActivityInteractor {

    private final ActivityService service;

    @Inject
    public ActivityInteractorImpl(ActivityService service) {
        this.service = service;

    }


    @Override
    public void getStartAds(ServiceResponse<StartBean> response) {
        service.getStartAds()
            .compose(new DefaultTransform<>())
            .subscribe(response);
    }
}
