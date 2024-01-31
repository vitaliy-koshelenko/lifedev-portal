package com.lifedev.dl.permission.propagator;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.propagator.BasePermissionPropagator;
import com.liferay.portal.kernel.security.permission.propagator.PermissionPropagator;
import com.liferay.portal.kernel.util.GetterUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import java.util.ArrayList;
import java.util.List;


@Component(
        property = {
                "javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY,
                "javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN
        },
        service = PermissionPropagator.class
)
public class DLPermissionPropagatorImpl extends BasePermissionPropagator {

    @Override
    public void propagateRolePermissions(ActionRequest actionRequest, String className,
                                         String primKey, long[] roleIds) throws PortalException {

        if (!className.equals(DLFolder.class.getName())) {
            _log.warn(String.format("Classname '%s' does not support permission propagation.", className));
            return;
        }

        long dlFolderId = getDLFolderId(primKey);
        DLFolder dlFolder = dlFolderLocalService.fetchDLFolder(dlFolderId);
        if (dlFolder == null) {
            _log.warn(String.format("DLFolder not found for primKey='%s'.", primKey));
            return;
        }

        String dlFolderName = dlFolder.getName();
        if (!isTargetFolder(dlFolder)) {
            _log.warn(String.format("Permission propagation is not supported for folder #%d '%s'.", dlFolderId, dlFolderName));
            return;
        }

        // Propagate DLFolder Permissions
        List<DLFolder> childFolders = getChildFolders(dlFolder);
        for (DLFolder childFolder: childFolders) {
            for (long roleId : roleIds) {
                propagateRolePermissions(
                        actionRequest, roleId, DLFolder.class.getName(), dlFolderId,
                        DLFolder.class.getName(), childFolder.getFolderId());
            }
            _log.info(String.format("Propagated permissions from DLFolder #%d '%s' to DLFolder #%d '%s'.",
                    dlFolderId, dlFolderName, childFolder.getFolderId(), childFolder.getName()));
        }

        // Propagate DLFile Permissions
        List<DLFileEntry> childFiles = getChildFiles(dlFolder);
        for (DLFileEntry fileEntry: childFiles) {
            for (long roleId : roleIds) {
                propagateRolePermissions(
                        actionRequest, roleId, DLFolder.class.getName(), dlFolderId,
                        DLFileEntry.class.getName(), fileEntry.getFileEntryId());
            }
            _log.info(String.format("Propagated permissions from DLFolder #%d '%s' to DLFileEntry #%d '%s'.",
                    dlFolderId, dlFolderName, fileEntry.getFileEntryId(), fileEntry.getFileName()));
        }

    }

    private List<DLFileEntry> getChildFiles(DLFolder parentFolder) {
        List<DLFolder> childFolders = dlFolderLocalService.getFolders(parentFolder.getGroupId(), parentFolder.getFolderId());
        List<DLFileEntry> childFiles = dlFileEntryLocalService.getFileEntries(parentFolder.getGroupId(), parentFolder.getFolderId());
        List<DLFileEntry> allFiles = new ArrayList<>(childFiles);
        for (DLFolder childFolder: childFolders) {
            allFiles.addAll(getChildFiles(childFolder));
        }
        return allFiles;
    }

    private List<DLFolder> getChildFolders(DLFolder parentFolder) {
        List<DLFolder> childFolders = dlFolderLocalService.getFolders(parentFolder.getGroupId(), parentFolder.getFolderId());
        List<DLFolder> allChildFolders = new ArrayList<>(childFolders);
        for (DLFolder childFolder: childFolders) {
            allChildFolders.addAll(getChildFolders(childFolder));
        }
        return allChildFolders;
    }

    private boolean isTargetFolder(DLFolder dlFolder) {
        // Implement, if permission propagation is needed only for specific folder(s)
        return true;
    }

    private static long getDLFolderId(String primKey) {
        try {
            return GetterUtil.getLong(primKey);
        } catch (Exception e) {
            _log.error(String.format("Can not cast primKey='%s' to number, cause: %s.", primKey, e.getMessage()));
            return GetterUtil.DEFAULT_LONG;
        }
    }

    @Reference
    private DLFolderLocalService dlFolderLocalService;
    @Reference
    private DLFileEntryLocalService dlFileEntryLocalService;

    private static final Log _log = LogFactoryUtil.getLog(DLPermissionPropagatorImpl.class);
}