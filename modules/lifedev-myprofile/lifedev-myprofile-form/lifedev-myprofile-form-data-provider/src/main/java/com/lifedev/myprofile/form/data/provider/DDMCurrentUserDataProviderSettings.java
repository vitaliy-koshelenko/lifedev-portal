package com.lifedev.myprofile.form.data.provider;

import com.liferay.dynamic.data.mapping.annotations.*;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderParameterSettings;

@DDMForm
@DDMFormLayout(
	{
		@DDMFormLayoutPage(
			{
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12, value = "outputParameters"
						)
					}
				)
			}
		)
	}
)
public interface DDMCurrentUserDataProviderSettings extends DDMDataProviderParameterSettings {

}