document.getElementById("profileForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent form submission

    // Get form data
    let form = this;

    // Get form field values
    let firstName = form.elements["firstName"].value;
    let lastName = form.elements["lastName"].value;
    let email = form.elements["email"].value;

    // Log form field values
    console.log("firstName:", firstName);
    console.log("lastName:", lastName);
    console.log("email:", email);

    console.log(JSON.stringify({
        "familyName": lastName,
        "givenName": firstName,
        "email": email
    }))

    /*let response = Liferay.Util.fetch(themeDisplay.getPortalURL() + "/o/headless-admin-user/v1.0/user-accounts/" + formData.get("userId"), {
        method: 'UPDATE',
        headers: {
            "Content-Type": "application/json",
            'x-csrf-token': Liferay.authToken,
        },
        body: JSON.stringify({
                givenName: "Ivan",
                familyName: "Volkov"
        })
        }).then(function(response) {
    return response.json();
}).then(function(response) {
    const {familyName, givenName} = response;
    console.log(response);
    console.log(givenName);
    console.log(familyName);
}).catch(function(error) {
    console.error(error);
})*/


    var xhr = new XMLHttpRequest();
    xhr.open("PATCH", themeDisplay.getPortalURL() + "/o/headless-admin-user/v1.0/user-accounts/20122");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("x-csrf-token", Liferay.authToken);
    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Request successful");
            console.log(xhr.responseText);
        } else {
            console.error("Request failed with status", xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error("Request failed");
    };
    xhr.send(JSON.stringify({
        "familyName": lastName,
        "givenName": firstName,
        "emailAddress": email
    }));

});