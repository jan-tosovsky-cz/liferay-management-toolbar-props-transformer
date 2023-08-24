package my.project.admin.web.internal.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import java.io.IOException;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import my.project.model.Project;
import my.project.service.ProjectLocalService;
import my.project.util.PortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.add-default-resource=true",
                "com.liferay.portlet.display-category=category.hidden",
                "com.liferay.portlet.layout-cacheable=true",
                "com.liferay.portlet.preferences-owned-by-group=true",
                "com.liferay.portlet.preferences-unique-per-layout=false",
                "com.liferay.portlet.private-request-attributes=false",
                "com.liferay.portlet.private-session-attributes=false",
                "com.liferay.portlet.render-weight=50",
                "com.liferay.portlet.use-default-template=true",
                "javax.portlet.expiration-cache=0",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + PortletKeys.PROJECT_ADMIN_PORTLET,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user,guest,administrator",
                "javax.portlet.version=3.0"
        },
        service = Portlet.class
)
public class ProjectsAdminPortlet extends MVCPortlet {

    @Reference
    private ProjectLocalService projectLocalService;

    @Override
    public void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

        long projectId = ParamUtil.getLong(renderRequest, "projectId");

        Project project = projectLocalService.fetchProject(projectId);

        if (project != null) {
            renderRequest.setAttribute("project", project);
        }

        super.doDispatch(renderRequest, renderResponse);
    }

}
