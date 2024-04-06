package com.lifedev.myprofile.form.storage.adapter;

import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceVersion;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceVersionLocalService;
import com.liferay.dynamic.data.mapping.storage.*;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.PortletSession;
import java.util.List;
import java.util.Locale;

@Component(
        property = "ddm.storage.adapter.type=user-storage-adapter",
        service = DDMStorageAdapter.class
)
public class UserDDMStorageAdapter implements DDMStorageAdapter {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    @Override
    public DDMStorageAdapterDeleteResponse delete(DDMStorageAdapterDeleteRequest ddmStorageAdapterDeleteRequest) throws StorageException {
        try {
            return defaultStorageAdapter.delete(ddmStorageAdapterDeleteRequest);
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public DDMStorageAdapterGetResponse get(DDMStorageAdapterGetRequest ddmStorageAdapterGetRequest) throws StorageException {
        try {
            return defaultStorageAdapter.get(ddmStorageAdapterGetRequest);
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public DDMStorageAdapterSaveResponse save(DDMStorageAdapterSaveRequest ddmStorageAdapterSaveRequest) throws StorageException {

        try {
            List<DDMFormFieldValue> ddmFormFieldValues = getDDMFormFieldValues(ddmStorageAdapterSaveRequest);
            DDMStorageAdapterSaveResponse defaultStorageAdapterSaveResponse = defaultStorageAdapter.save(ddmStorageAdapterSaveRequest);

            PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
            User currentUser = permissionChecker.getUser();

            for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
                Locale locale = LocaleUtil.getDefault();
                String fieldName = ddmFormFieldValue.getFieldReference();
                String fieldValue = ddmFormFieldValue.getValue().getString(locale);

                _log.info("UserDDMStorageAdapter, field [" + fieldName + "]: " + fieldValue);

                if (FIRST_NAME.equals(fieldName)){
                    currentUser.setFirstName(fieldValue);
                } else if (LAST_NAME.equals(fieldName)) {
                    currentUser.setLastName(fieldValue);
                }
            }
            userLocalService.updateUser(currentUser);
            return defaultStorageAdapterSaveResponse;
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

    private List<DDMFormFieldValue> getDDMFormFieldValues(DDMStorageAdapterSaveRequest ddmStorageAdapterSaveRequest) throws PortalException {
        try {
            return getDDMFormFieldValuesFromPortletRequest();
        } catch (Exception e) {
            _log.warn("Failed to get DDMFormFieldValue from request, cause: " + e.getMessage());
            return getDDMFormFieldValuesFromStorageAdapterRequest(ddmStorageAdapterSaveRequest);
        }
    }

    private List<DDMFormFieldValue> getDDMFormFieldValuesFromPortletRequest() throws PortalException {
        List<DDMFormFieldValue> ddmFormFieldValues = null;
        ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
        LiferayPortletRequest portletRequest = serviceContext.getLiferayPortletRequest();
        long formInstanceId = ParamUtil.getLong(portletRequest, "formInstanceId");
        if (formInstanceId == 0) {
            PortletSession portletSession = portletRequest.getPortletSession();
            formInstanceId = GetterUtil.getLong(portletSession.getAttribute("DYNAMIC_DATA_MAPPING_FORM_INSTANCE_ID"));
        }
        DDMFormInstance ddmFormInstance = ddmFormInstanceLocalService.fetchDDMFormInstance(formInstanceId);
        if (ddmFormInstance != null) {
            DDMForm ddmForm = getDDMForm(ddmFormInstance);
            DDMFormValues ddmFormValues = ddmFormValuesFactory.create(portletRequest, ddmForm);
            ddmFormFieldValues = ddmFormValues.getDDMFormFieldValues();
        }
        return ddmFormFieldValues;
    }

    private List<DDMFormFieldValue> getDDMFormFieldValuesFromStorageAdapterRequest(DDMStorageAdapterSaveRequest ddmStorageAdapterSaveRequest) throws PortalException {
        DDMFormValues ddmFormValues = ddmStorageAdapterSaveRequest.getDDMFormValues();
        return ddmFormValues.getDDMFormFieldValues();
    }

    private DDMForm getDDMForm(DDMFormInstance ddmFormInstance) throws PortalException {
        DDMFormInstanceVersion latestDDMFormInstanceVersion =
                ddmFormInstanceVersionLocalService.getLatestFormInstanceVersion(ddmFormInstance.getFormInstanceId(),
                        WorkflowConstants.STATUS_APPROVED);
        DDMStructureVersion ddmStructureVersion = latestDDMFormInstanceVersion.getStructureVersion();
        return ddmStructureVersion.getDDMForm();
    }

    @Reference
    private UserLocalService userLocalService;
    @Reference(target = "(ddm.storage.adapter.type=default)")
    private DDMStorageAdapter defaultStorageAdapter;
    @Reference
    private DDMFormValuesFactory ddmFormValuesFactory;
    @Reference
    private DDMFormInstanceLocalService ddmFormInstanceLocalService;
    @Reference
    private DDMFormInstanceVersionLocalService ddmFormInstanceVersionLocalService;

    private static final Log _log = LogFactoryUtil.getLog(UserDDMStorageAdapter.class);
}
