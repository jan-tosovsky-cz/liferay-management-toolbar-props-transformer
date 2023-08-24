package my.project.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
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
                "mvc.command.name=/update-project-status"
        },
        service = MVCActionCommand.class
)
public class UpdateProjectStatusMVCActionCommand extends BaseMVCActionCommand {

    @Reference
    private ProjectLocalService projectLocalService;

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(actionRequest);

        long userId = serviceContext.getUserId();
        long projectId = ParamUtil.getLong(actionRequest, "projectId");
        int status = ParamUtil.getInteger(actionRequest, "status");

        projectLocalService.updateProjectStatus(userId, projectId, status, serviceContext);
    }
}
