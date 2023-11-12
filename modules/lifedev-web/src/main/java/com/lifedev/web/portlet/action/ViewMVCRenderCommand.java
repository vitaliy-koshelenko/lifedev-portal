package com.lifedev.web.portlet.action;

import com.lifedev.web.constants.MyProfilePortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import org.osgi.service.component.annotations.Component;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
        property = {
                "javax.portlet.name=" + MyProfilePortletKeys.PORTLET_ID,
                "mvc.command.name=" + MyProfilePortletKeys.MVC_COMMAND_DEFAULT
        },
        service = MVCRenderCommand.class
)
public class ViewMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) {

        return MyProfilePortletKeys.VIEW_JSP;
    }

}