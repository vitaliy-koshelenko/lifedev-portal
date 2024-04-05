package com.lifedev.myprofile.form.storage.adapter;

import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.storage.*;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Locale;

@Component(
        property = "ddm.storage.adapter.type=user-storage-adapter",
        service = DDMStorageAdapter.class
)
public class UserDDMStorageAdapter implements DDMStorageAdapter {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";

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
            DDMStorageAdapterSaveResponse defaultStorageAdapterSaveResponse = defaultStorageAdapter.save(ddmStorageAdapterSaveRequest);

            PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
            User currentUser = permissionChecker.getUser();

            List<DDMFormFieldValue> ddmFormFieldValues = ddmStorageAdapterSaveRequest.getDDMFormValues().getDDMFormFieldValues();
            for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
                Locale locale = LocaleUtil.getDefault();
                String fieldName = ddmFormFieldValue.getFieldReference();
                String fieldValue = ddmFormFieldValue.getValue().getString(locale);
                if (FIRST_NAME.equals(fieldName)){
                    currentUser.setFirstName(fieldValue);
                } else if (LAST_NAME.equals(fieldName)) {
                    currentUser.setLastName(fieldValue);
                } else if (EMAIL.equals(fieldName)) {
                    currentUser.setEmailAddress(fieldValue);
                }
            }
            userLocalService.updateUser(currentUser);
            return defaultStorageAdapterSaveResponse;
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

    @Reference
    private UserLocalService userLocalService;
    @Reference(target = "(ddm.storage.adapter.type=default)")
    private DDMStorageAdapter defaultStorageAdapter;

}
