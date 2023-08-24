package my.project.admin.web.internal.portlet;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.BaseControlPanelEntry;
import com.liferay.portal.kernel.portlet.ControlPanelEntry;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import my.project.util.PortletKeys;
import org.osgi.service.component.annotations.Component;

@Component(
        property = "javax.portlet.name=" + PortletKeys.PROJECT_ADMIN_PORTLET,
        service = ControlPanelEntry.class
)
public class ProjectsAdminControlPanelEntry extends BaseControlPanelEntry {

    @Override
    protected boolean hasAccessPermissionDenied(
            PermissionChecker permissionChecker, Group group, Portlet portlet)
            throws Exception {

        if (group.isLayout()) {
            return true;
        }

        return super.hasAccessPermissionDenied(
                permissionChecker, group, portlet);
    }
}
