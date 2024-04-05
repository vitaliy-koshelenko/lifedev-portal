package com.lifedev.myprofile.form.data.provider.builder;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;

public interface UserInfoBuilder {

    String FIRST_NAME = "firstName";
    String LAST_NAME = "lastName";
    String EMAIL = "email";

    JSONObject buildUserInfo(User user);

}