<%@include file="/init.jsp" %>

<%
    Project project = (Project) renderRequest.getAttribute("project");
%>

<c:choose>
    <c:when test="<%= project != null %>">
        <liferay-util:include page="/view-project.jsp" servletContext="<%= application%>"/>
    </c:when>
    <c:otherwise>
        <liferay-util:include page="/view-project-list.jsp" servletContext="<%= application%>"/>
    </c:otherwise>
</c:choose>
