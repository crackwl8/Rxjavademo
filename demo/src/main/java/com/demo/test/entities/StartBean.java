package com.demo.test.entities;

import com.google.gson.annotations.SerializedName;

/*
* 网络上获得数据结构
* */
public class StartBean {


  @SerializedName("picture") private String mPicture;
  @SerializedName("created") private String mCreated;

  public String getPicture() {
    return mPicture;
  }

  public void setPicture(String picture) {
    mPicture = picture;
  }

  public String getCreated() {
    return mCreated;
  }

  public void setCreated(String created) {
    mCreated = created;
  }
}
