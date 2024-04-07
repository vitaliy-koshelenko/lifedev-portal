const form = fragmentElement.querySelector("form");

form.addEventListener("submit", function(event) {
	event.preventDefault();
	let form = this;

	let userId = Liferay.ThemeDisplay.getUserId();
	let firstName = form.elements["firstName"].value;
	let lastName = form.elements["lastName"].value;
	let email = form.elements["email"].value;

	const updateProfileURL = themeDisplay.getPortalURL() + "/o/headless-admin-user/v1.0/user-accounts/" + userId;
	Liferay.Util.fetch(updateProfileURL, {
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
			message: 'User has been updated successfully.',
			type: 'success'
		});
	}).catch(function(error) {
		console.error("Error:", error);
		Liferay.Util.openToast({
			message: 'Error occurred while updating user:' + error,
			type: 'danger'
		});
	});
});