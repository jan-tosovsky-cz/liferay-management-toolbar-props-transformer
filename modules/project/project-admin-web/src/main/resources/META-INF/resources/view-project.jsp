<%@include file="/init.jsp" %>

<%
    Project project = (Project) renderRequest.getAttribute("project");

    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);
    renderResponse.setTitle(HtmlUtil.escape(project.getName()));
%>

<p>TODO</p>
