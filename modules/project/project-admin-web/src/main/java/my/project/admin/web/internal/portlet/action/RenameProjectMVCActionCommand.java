package my.project.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import my.project.admin.web.internal.portlet.handler.ProjectExceptionRequestHandler;
import my.project.service.ProjectLocalService;
import my.project.util.PortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "javax.portlet.name=" + PortletKeys.PROJECT_ADMIN_PORTLET,
                "mvc.command.name=/rename-project"
        },
        service = MVCActionCommand.class
)
public class RenameProjectMVCActionCommand extends BaseMVCActionCommand {

    @Reference
    private ProjectLocalService projectLocalService;

    @Reference
    private ProjectExceptionRequestHandler projectExceptionRequestHandler;

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
                WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();
        long projectId = ParamUtil.getLong(actionRequest, "projectId");
        String name = ParamUtil.getString(actionRequest, "name");
        String redirect = ParamUtil.getString(actionRequest, "redirect");

        try {
            ServiceContext serviceContext = ServiceContextFactory.getInstance(
                    actionRequest);

            projectLocalService.renameProject(userId, projectId, name, serviceContext);

            JSONObject jsonObject = JSONUtil.put("redirectURL", redirect);

            JSONPortletResponseUtil.writeJSON(
                    actionRequest, actionResponse, jsonObject);

        } catch (PortalException portalException) {
            hideDefaultSuccessMessage(actionRequest);

            projectExceptionRequestHandler.handlePortalException(
                    actionRequest, actionResponse, portalException);
        }
    }
}
