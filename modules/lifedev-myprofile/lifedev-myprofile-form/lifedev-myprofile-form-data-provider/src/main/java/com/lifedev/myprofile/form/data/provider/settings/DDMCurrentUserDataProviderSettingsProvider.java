package com.lifedev.myprofile.form.data.provider.settings;

import com.liferay.dynamic.data.mapping.data.provider.settings.DDMDataProviderSettingsProvider;
import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true, property = "ddm.data.provider.type=user",
	service = DDMDataProviderSettingsProvider.class
)
public class DDMCurrentUserDataProviderSettingsProvider implements DDMDataProviderSettingsProvider {

	@Override
	public Class<?> getSettings() {
		return DDMDataProviderSettingsProvider.class;
	}

}