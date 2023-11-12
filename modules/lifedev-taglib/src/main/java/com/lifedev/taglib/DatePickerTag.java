package com.lifedev.taglib;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.service.Snapshot;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.Date;

public class DatePickerTag extends IncludeTag {

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        setServletContext(_servletContextSnapshot.get());
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
        _name = null;
        _label = null;
        _date = null;
    }

    @Override
    protected String getPage() {
        return _PAGE;
    }

    @Override
    protected void setAttributes(HttpServletRequest httpServletRequest) {
        httpServletRequest.setAttribute("lifedev:datepicker:name", _name);
        httpServletRequest.setAttribute("lifedev:datepicker:label", _label);
        httpServletRequest.setAttribute("lifedev:datepicker:date", _date);
    }

    public void setName(String name) {
        _name = name;
    }
    public void setLabel(String label) {
        _label = label;
    }
    public void setDate(Date date) {
        _date = date;
    }

    public String getName() {
        return _name;
    }
    public String getLabel() {
        return _label;
    }
    public Date getDate() {
        return _date;
    }

    private String _name;
    private String _label;
    private Date _date;

    private static final String _PAGE = "/lifedev/datepicker.jsp";

    private static final Snapshot<ServletContext> _servletContextSnapshot =
            new Snapshot<>(DatePickerTag.class, ServletContext.class, "(osgi.web.symbolicname=com.lifedev.taglib)");

    private static final Log _log = LogFactoryUtil.getLog(DatePickerTag.class);
}