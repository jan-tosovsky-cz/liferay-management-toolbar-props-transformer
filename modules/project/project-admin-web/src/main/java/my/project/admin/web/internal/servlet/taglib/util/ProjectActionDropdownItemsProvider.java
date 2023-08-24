package my.project.admin.web.internal.servlet.taglib.util;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import my.project.model.Project;

public class ProjectActionDropdownItemsProvider {

    private final HttpServletRequest httpServletRequest;
    private final LiferayPortletResponse liferayPortletResponse;
    private final Project project;
    private final ThemeDisplay themeDisplay;

    public ProjectActionDropdownItemsProvider(
            LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse,
            Project project) {

        this.liferayPortletResponse = liferayPortletResponse;
        this.project = project;

        httpServletRequest = PortalUtil.getHttpServletRequest(liferayPortletRequest);
        themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);
    }

    public List<DropdownItem> getActionDropdownItems() throws Exception {

        // FIXME
        boolean hasUpdatePermission = true;
        boolean isReadyToDelete = project.getStatus() == WorkflowConstants.STATUS_INACTIVE;

        return DropdownItemListBuilder.addGroup(
                dropdownGroupItem -> {
                    dropdownGroupItem.setDropdownItems(
                            DropdownItemListBuilder.add(
                                    () -> hasUpdatePermission,
                                    getRenameActionUnsafeConsumer()
                            ).build());
                    dropdownGroupItem.setSeparator(true);
                }
        ).addGroup(
                dropdownGroupItem -> {
                    dropdownGroupItem.setDropdownItems(
                            DropdownItemListBuilder.add(
                                    () -> hasUpdatePermission,
                                    getUpdateStatusActionUnsafeConsumer()
                            ).add(
                                    () -> hasUpdatePermission && isReadyToDelete,
                                    getDeleteActionUnsafeConsumer()
                            ).build());
                    dropdownGroupItem.setSeparator(true);
                }
        ).build();
    }

    private UnsafeConsumer<DropdownItem, Exception> getRenameActionUnsafeConsumer() {

        return dropdownItem -> {
            dropdownItem.putData("action", "renameProject");
            dropdownItem.putData(
                    "idFieldValue",
                    String.valueOf(project.getProjectId()));
            dropdownItem.putData(
                    "mainFieldValue", project.getName());
            dropdownItem.putData(
                    "renameProjectURL",
                    PortletURLBuilder.createActionURL(
                            liferayPortletResponse
                    ).setActionName(
                            "/rename-project"
                    ).setRedirect(
                            themeDisplay.getURLCurrent()
                    ).setParameter(
                            "projectId",
                            project.getProjectId()
                    ).buildString());
            dropdownItem.setIcon("pencil");
            dropdownItem.setLabel(
                    LanguageUtil.get(httpServletRequest, "rename"));
        };
    }

    private UnsafeConsumer<DropdownItem, Exception> getUpdateStatusActionUnsafeConsumer() {

        boolean isApproved = project.getStatus() == WorkflowConstants.STATUS_APPROVED;

        return dropdownItem -> {
            dropdownItem.putData("action", "updateProjectStatus");
            dropdownItem.putData(
                    "updateProjectStatusURL",
                    PortletURLBuilder.createActionURL(
                            liferayPortletResponse
                    ).setActionName(
                            "/update-project-status"
                    ).setRedirect(
                            themeDisplay.getURLCurrent()
                    ).setParameter(
                            "projectId",
                            project.getProjectId()
                    ).setParameter(
                            "status",
                            isApproved ? WorkflowConstants.STATUS_INACTIVE : WorkflowConstants.STATUS_APPROVED
                    ).buildString());
            dropdownItem.setIcon(isApproved ? "hidden" : "view");
            dropdownItem.setLabel(
                    LanguageUtil.get(httpServletRequest, isApproved ? "deactivate" : "activate"));
        };
    }

    private UnsafeConsumer<DropdownItem, Exception> getDeleteActionUnsafeConsumer() {

        return dropdownItem -> {
            dropdownItem.putData("action", "deleteProject");
            dropdownItem.putData(
                    "deleteProjectURL",
                    PortletURLBuilder.createActionURL(
                            liferayPortletResponse
                    ).setActionName(
                            "/delete-project"
                    ).setRedirect(
                            themeDisplay.getURLCurrent()
                    ).setParameter(
                            "projectId",
                            project.getProjectId()
                    ).buildString());
            dropdownItem.setIcon("trash");
            dropdownItem.setLabel(
                    LanguageUtil.get(httpServletRequest, "delete"));
        };
    }

}
