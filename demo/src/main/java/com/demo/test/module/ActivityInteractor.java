package com.demo.test.module;

import com.demo.test.entities.StartBean;
import com.demo.test.service.ServiceResponse;


public interface ActivityInteractor {

    void getStartAds(ServiceResponse<StartBean> response);
}
