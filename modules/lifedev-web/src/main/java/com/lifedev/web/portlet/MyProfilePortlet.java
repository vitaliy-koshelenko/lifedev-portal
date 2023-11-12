package com.lifedev.web.portlet;

import com.lifedev.web.constants.MyProfilePortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import org.osgi.service.component.annotations.Component;

import javax.portlet.Portlet;

@Component(
	property = {
			"com.liferay.portlet.display-category=" + MyProfilePortletKeys.CATEGORY_NAME,
			"com.liferay.portlet.instanceable=false",
			"javax.portlet.name=" + MyProfilePortletKeys.PORTLET_ID,
			"javax.portlet.display-name=" + MyProfilePortletKeys.DISPLAY_NAME,
			"javax.portlet.init-param.template-path=/META-INF/resources/",
			"javax.portlet.init-param.view-template=" + MyProfilePortletKeys.VIEW_JSP,
			"javax.portlet.resource-bundle=content.Language",
			"javax.portlet.security-role-ref=power-user,user",
			"com.liferay.portlet.css-class-wrapper=" + MyProfilePortletKeys.CSS_CLASS_WRAPPER,
			"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class MyProfilePortlet extends MVCPortlet {

}