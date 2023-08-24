package my.project.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import my.project.service.ProjectLocalService;
import my.project.util.PortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "javax.portlet.name=" + PortletKeys.PROJECT_ADMIN_PORTLET,
                "mvc.command.name=/delete-project"
        },
        service = MVCActionCommand.class
)
public class DeleteProjectMVCActionCommand extends BaseMVCActionCommand {

    @Reference
    private ProjectLocalService projectLocalService;

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        long projectId = ParamUtil.getLong(actionRequest, "projectId");

        projectLocalService.deleteProject(projectId);
    }
}
