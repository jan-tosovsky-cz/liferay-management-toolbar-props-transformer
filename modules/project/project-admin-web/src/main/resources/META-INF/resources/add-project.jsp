<%@include file="/init.jsp" %>

<%
    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);
    renderResponse.setTitle(LanguageUtil.get(request, "add-new-project"));
%>

<portlet:actionURL name="/add-project" var="addProjectURL">
    <portlet:param name="mvcPath" value="/add-project.jsp" />
    <portlet:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
</portlet:actionURL>

<aui:form action="<%= addProjectURL %>" cssClass="container-fluid container-fluid-max-xl" name="fm">
    <aui:input name="redirect" type="hidden" value="<%= redirect %>" />

    <aui:model-context model="<%= Project.class %>" />

    <div class="sheet">
        <div class="panel-group panel-group-flush">
            <aui:fieldset>
                <aui:input label="name" name="name" placeholder="name" />
            </aui:fieldset>
        </div>
    </div>

    <aui:button-row>
        <aui:button type="submit" />
        <aui:button href="<%= redirect %>" type="cancel" />
    </aui:button-row>
</aui:form>
