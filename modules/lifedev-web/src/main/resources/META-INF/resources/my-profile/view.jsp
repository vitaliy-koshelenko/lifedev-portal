<%@ include file="init.jsp" %>

<portlet:actionURL name="<%= MyProfilePortletKeys.MVC_COMMAND_EDIT_PROFILE %>" var="editProfileURL"/>

<aui:form action="${editProfileURL}" method="post">
    <div class="row">
        <div class="col-6">
            <aui:input name="firstName" label="my-profile.first-name" value="${user.firstName}"/>
        </div>
        <div class="col-6">
            <aui:input name="lastName" label="my-profile.last-name" value="${user.lastName}"/>
        </div>
    </div>
    <div class="row">
        <div class="col-6">
            <aui:input name="emailAddress" label="my-profile.email-address" value="${user.emailAddress}"/>
        </div>
        <div class="col-6">
            <lifedev:datepicker name="dateOfBirth" label="my-profile.date-of-birth" date="${user.birthday}" />
        </div>
    </div>
    <div class="row">
        <div class="col-12 text-right">
            <aui:button type="submit"/>
        </div>
    </div>
</aui:form>