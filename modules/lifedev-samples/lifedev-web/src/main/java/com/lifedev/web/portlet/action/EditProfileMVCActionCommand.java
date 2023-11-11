package com.lifedev.web.portlet.action;

import com.lifedev.web.constants.MyProfilePortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.ListType;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.*;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

            String firstName = ParamUtil.getString(actionRequest, "firstName");
            String lastName = ParamUtil.getString(actionRequest, "lastName");
            String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
            Date dateOfBirth = ParamUtil.getDate(actionRequest, "dateOfBirth", dateFormat);
            String phoneNumber = ParamUtil.getString(actionRequest, "phoneNumber");
            ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);

            updateUser(user, dateOfBirth, emailAddress, firstName, lastName, phoneNumber, serviceContext);

        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }
    }

    private void updateUser(User user, Date dateOfBirth, String emailAddress, String firstName, String lastName,
                            String phoneNumber, ServiceContext serviceContext) throws PortalException {
        // Update User
        Contact contact = user.getContact();
        Calendar calendar = CalendarFactoryUtil.getCalendar();
        calendar.setTime(dateOfBirth);
        user = userLocalService.updateUser(
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
        // Update Phone
        List<Phone> phones = user.getPhones();
        if (ListUtil.isNotEmpty(phones)) {
            Phone phone = phones.get(0);
            phoneLocalService.updatePhone(
                    phone.getPhoneId(),
                    phoneNumber,
                    StringPool.BLANK,
                    phone.getListTypeId(),
                    phone.isPrimary()
            );
        } else {
            ListType listType = listTypeService.getListType(user.getCompanyId(), "personal", Contact.class.getName() + ".phone");
            phoneLocalService.addPhone(
                    user.getUserId(),
                    Contact.class.getName(),
                    contact.getContactId(),
                    phoneNumber,
                    StringPool.BLANK,
                    listType.getListTypeId(),
                    true,
                    serviceContext
            );
        }
    }

    @Reference
    private ListTypeService listTypeService;
    @Reference
    private UserLocalService userLocalService;
    @Reference
    private PhoneLocalService phoneLocalService;

    private static final Log _log = LogFactoryUtil.getLog(EditProfileMVCActionCommand.class);
}
