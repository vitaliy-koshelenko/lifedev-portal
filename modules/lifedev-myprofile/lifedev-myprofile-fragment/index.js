fragmentElement.querySelector("#myProfileForm").addEventListener("submit", function(event) {
	event.preventDefault();
	let form = this;

	let firstName = form.elements["firstName"].value;
	let lastName = form.elements["lastName"].value;
	let email = form.elements["email"].value;
	let userId = Liferay.ThemeDisplay.getUserId();

	let response = Liferay.Util.fetch(themeDisplay.getPortalURL() + "/o/headless-admin-user/v1.0/user-accounts/" + userId, {
		method: 'PATCH',
		headers: {
			"Content-Type": "application/json",
			'x-csrf-token': Liferay.authToken,
		},
		body: JSON.stringify({
			"familyName": lastName,
			"givenName": firstName,
			"emailAddress": email
		})
	}).then(function(response) {
		if (!response.ok) {
			throw new Error("HTTP error, status = " + response.status);
		}
		return response.json();
	}).then(function(response) {
		console.log(response);
		Liferay.Util.openToast({
			message: 'User has been updated.',
			type: 'success'
		});
	}).catch(function(error) {
		console.error("Error:", error);
		Liferay.Util.openToast({
			message: 'Error occurred while updating user.',
			type: 'danger'
		});
	});
});

fragmentElement.querySelector("#cancel-button").addEventListener("click", function(event) {
	window.location.reload();
});