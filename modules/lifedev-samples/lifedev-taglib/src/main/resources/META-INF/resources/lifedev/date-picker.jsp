<%@ include file="../init.jsp" %>

<%
    String fieldName = (String) request.getAttribute("lifedev:date-picker:name");
    String fieldLabel = (String) request.getAttribute("lifedev:date-picker:label");
    Date date = (Date) request.getAttribute("lifedev:date-picker:date");
    if (date == null) {
        date = new Date();
    }
    Calendar calendar = CalendarFactoryUtil.getCalendar();
    calendar.setTime(date);
%>

<div class="form-field">
    <label for="<%= fieldName %>">
        <liferay-ui:message key="<%= fieldLabel %>" />
    </label>
    <liferay-ui:input-date name="<%= fieldName %>"
                           yearValue="<%= calendar.get(Calendar.YEAR) %>"
                           monthValue="<%= calendar.get(Calendar.MONTH) %>"
                           dayValue="<%= calendar.get(Calendar.DATE) %>"
    />
</div>