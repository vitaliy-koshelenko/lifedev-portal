package com.lifedev.web.portlet.action;

import com.lifedev.web.constants.MyProfilePortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.text.DateFormat;
import java.util.List;

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
        try {
            User user = portal.getUser(renderRequest);

            String phoneNumber = StringPool.BLANK;
            List<Phone> phones = user.getPhones();
            if (ListUtil.isNotEmpty(phones)) {
                Phone phone = phones.get(0);
                phoneNumber = phone.getNumber();
            }
            renderRequest.setAttribute("phoneNumber", phoneNumber);

            ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
            DateFormat dateFormat = DateFormatFactoryUtil.getDate(themeDisplay.getLocale());
            String dateOfBirth = dateFormat.format(user.getBirthday());
            renderRequest.setAttribute("dateOfBirth", dateOfBirth);

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
        return MyProfilePortletKeys.VIEW_JSP;
    }

    @Reference
    private Portal portal;

    private static final Log _log = LogFactoryUtil.getLog(ViewMVCRenderCommand.class);
}