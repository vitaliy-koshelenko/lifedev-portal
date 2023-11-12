package com.lifedev.web.portlet.action;

import com.lifedev.web.constants.MyProfileKeys;
import com.lifedev.web.constants.MyProfilePortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@Component(
        property = {
                "javax.portlet.name=" + MyProfilePortletKeys.PORTLET_ID,
                "mvc.command.name=" + MyProfilePortletKeys.MVC_COMMAND_EDIT_PROFILE
        },
        service = MVCActionCommand.class
)
public class EditProfileMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
            User user = themeDisplay.getUser();
            DateFormat dateFormat = DateFormatFactoryUtil.getDate(themeDisplay.getLocale());

            String firstName = ParamUtil.getString(actionRequest, MyProfileKeys.FIRST_NAME);
            String lastName = ParamUtil.getString(actionRequest, MyProfileKeys.LAST_NAME);
            String emailAddress = ParamUtil.getString(actionRequest, MyProfileKeys.EMAIL_ADDRESS);
            Date dateOfBirth = ParamUtil.getDate(actionRequest, MyProfileKeys.DATE_OF_BIRTH, dateFormat);
            ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);

            updateUser(user, dateOfBirth, emailAddress, firstName, lastName, serviceContext);

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
    }

    private void updateUser(User user, Date dateOfBirth, String emailAddress,
                            String firstName, String lastName, ServiceContext serviceContext) throws PortalException {
        // Update User
        Contact contact = user.getContact();
        Calendar calendar = CalendarFactoryUtil.getCalendar();
        calendar.setTime(dateOfBirth);
        userLocalService.updateUser(
                user.getUserId(),
                StringPool.BLANK,
                StringPool.BLANK,
                StringPool.BLANK,
                false,
                user.getReminderQueryQuestion(),
                user.getReminderQueryAnswer(),
                user.getScreenName(),
                emailAddress,
                true,
                null,
                user.getLanguageId(),
                user.getTimeZoneId(),
                user.getGreeting(),
                user.getComments(),
                firstName,
                user.getMiddleName(),
                lastName,
                contact.getPrefixListTypeId(),
                contact.getSuffixListTypeId(),
                user.isMale(),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR),
                contact.getSmsSn(),
                contact.getFacebookSn(),
                contact.getJabberSn(),
                contact.getSkypeSn(),
                contact.getTwitterSn(),
                contact.getJobTitle(),
                user.getGroupIds(),
                user.getOrganizationIds(),
                user.getRoleIds(),
                null,
                user.getUserGroupIds(),
                serviceContext
        );
    }

    @Reference
    private UserLocalService userLocalService;

    private static final Log _log = LogFactoryUtil.getLog(EditProfileMVCActionCommand.class);
}
