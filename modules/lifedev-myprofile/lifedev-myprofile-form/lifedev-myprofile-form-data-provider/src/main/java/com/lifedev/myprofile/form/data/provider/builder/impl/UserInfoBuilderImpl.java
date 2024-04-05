package com.lifedev.myprofile.form.data.provider.builder.impl;

import com.lifedev.myprofile.form.data.provider.builder.UserInfoBuilder;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        service = UserInfoBuilder.class
)
public class UserInfoBuilderImpl implements UserInfoBuilder {

    @Override
    public JSONObject buildUserInfo(User user) {
        JSONObject userInfo = JSONFactoryUtil.createJSONObject();
        try {
            userInfo.put(FIRST_NAME, user.getFirstName());
            userInfo.put(LAST_NAME, user.getLastName());
            userInfo.put(EMAIL, user.getEmailAddress());
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
        return userInfo;
    }

    private static final Log _log = LogFactoryUtil.getLog(UserInfoBuilderImpl.class);

}