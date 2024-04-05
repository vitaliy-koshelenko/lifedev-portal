package com.lifedev.myprofile.form.data.provider;

import com.lifedev.myprofile.form.data.provider.builder.UserInfoBuilder;
import com.liferay.dynamic.data.mapping.data.provider.*;
import com.liferay.dynamic.data.mapping.data.provider.settings.DDMDataProviderSettingsProvider;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Optional;

@Component(
	immediate = true,
	property = "ddm.data.provider.type=user",
	service = DDMDataProvider.class
)
public class DDMCurrentUserDataProvider implements DDMDataProvider {

	private static final String TYPE_TEXT = "text";

	@Override
	public DDMDataProviderResponse getData(DDMDataProviderRequest ddmDataProviderRequest) throws DDMDataProviderException {
		try {
			return doGetData(ddmDataProviderRequest);
		}
		catch (Exception exception) {
			throw new DDMDataProviderException(exception);
		}
	}

	@Override
	public Class<?> getSettings() {
		return ddmDataProviderSettingsProvider.getSettings();
	}

	protected DDMDataProviderResponse createDDMDataProviderResponse
			(DDMCurrentUserDataProviderSettings ddmCurrentUserDataProviderSettings, JSONObject userInfo) {

		DDMDataProviderResponse.Builder builder = DDMDataProviderResponse.Builder.newBuilder();

		for (DDMDataProviderOutputParametersSettings outputParameterSettings : ddmCurrentUserDataProviderSettings.outputParameters()) {

			String outputParameterId = outputParameterSettings.outputParameterId();
			String outputParameterPath = outputParameterSettings.outputParameterPath();
			String outputParameterType = outputParameterSettings.outputParameterType();

			if (TYPE_TEXT.equals(outputParameterType)) {
				String paramValue = userInfo.getString(outputParameterPath);
				builder = builder.withOutput(outputParameterId, paramValue);
			}
		}

		return builder.build();
	}

	protected DDMDataProviderResponse doGetData(DDMDataProviderRequest ddmDataProviderRequest) throws Exception {

		Optional<DDMDataProviderInstance> ddmDataProviderInstance = fetchDDMDataProviderInstance(ddmDataProviderRequest.getDDMDataProviderId());

		DDMCurrentUserDataProviderSettings ddmCurrentUserDataProviderSettings =
			ddmDataProviderInstanceSettings.getSettings(ddmDataProviderInstance.get(), DDMCurrentUserDataProviderSettings.class);

		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
		User currentUser = permissionChecker.getUser();

		JSONObject currentUserJSONObject = userInfoBuilder.buildUserInfo(currentUser);

		return createDDMDataProviderResponse(ddmCurrentUserDataProviderSettings, currentUserJSONObject);
	}

	protected Optional<DDMDataProviderInstance> fetchDDMDataProviderInstance(String ddmDataProviderInstanceId) throws Exception {
		DDMDataProviderInstance ddmDataProviderInstance = ddmDataProviderInstanceService.fetchDataProviderInstanceByUuid(ddmDataProviderInstanceId);
		if ((ddmDataProviderInstance == null) && Validator.isNumber(ddmDataProviderInstanceId)) {
			ddmDataProviderInstance = ddmDataProviderInstanceService.fetchDataProviderInstance(Long.parseLong(ddmDataProviderInstanceId));
		}
		return Optional.ofNullable(ddmDataProviderInstance);
	}

	@Reference
	protected UserInfoBuilder userInfoBuilder;
	@Reference
	protected DDMDataProviderInstanceService ddmDataProviderInstanceService;

	@Reference
	protected DDMDataProviderInstanceSettings ddmDataProviderInstanceSettings;

	@Reference(target = "(ddm.data.provider.type=user)")
	protected DDMDataProviderSettingsProvider ddmDataProviderSettingsProvider;

}