<%@include file="/init.jsp" %>

<%
    ProjectSearchContainer projectSearchContainer = new ProjectSearchContainer(liferayPortletRequest, currentURLObj);

    ProjectManagementToolbarDisplayContext projectManagementToolbarDisplayContext =
            new ProjectManagementToolbarDisplayContext(
                    liferayPortletRequest, liferayPortletResponse, request, projectSearchContainer);
%>

<clay:management-toolbar
        displayContext="<%= projectManagementToolbarDisplayContext %>"
        propsTransformer="js/ProjectManagementToolbarPropsTransformer"
/>

<liferay-ui:search-container
        searchContainer="<%= projectSearchContainer%>"
        cssClass="container-fluid container-fluid-max-xl">

    <liferay-ui:search-container-row
            className="my.project.model.Project"
            escapedModel="<%= true %>"
            keyProperty="projectId"
            modelVar="project">

        <liferay-portlet:renderURL varImpl="rowURL">
            <portlet:param name="mvcPath" value="/view-project.jsp" />
            <portlet:param name="projectId" value="<%= String.valueOf(project.getProjectId())%>" />
            <portlet:param name="redirect" value="<%= currentURL%>" />
        </liferay-portlet:renderURL>

        <liferay-ui:search-container-column-text
                href="<%= rowURL%>"
                property="name"
        />

        <liferay-ui:search-container-column-text name="status">
            <clay:label label="<%= projectSearchContainer.getStatusMap().get(String.valueOf(project.getStatus())) %>" displayType="<%= WorkflowConstants.getStatusStyle(project.getStatus())%>" />
        </liferay-ui:search-container-column-text>

        <liferay-ui:search-container-column-text align="right">

            <%
                ProjectActionDropdownItemsProvider projectActionDropdownItemsProvider = new ProjectActionDropdownItemsProvider(liferayPortletRequest, liferayPortletResponse, project);
            %>

            <clay:dropdown-actions
                    aria-label='<%= LanguageUtil.get(request, "show-actions") %>'
                    dropdownItems="<%= projectActionDropdownItemsProvider.getActionDropdownItems() %>"
                    propsTransformer="js/ProjectActionDropdownDefaultPropsTransformer"
            />
        </liferay-ui:search-container-column-text>

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon" />

</liferay-ui:search-container>
