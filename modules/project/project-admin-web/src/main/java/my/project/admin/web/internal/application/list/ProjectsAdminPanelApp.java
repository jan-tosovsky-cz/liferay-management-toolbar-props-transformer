package my.project.admin.web.internal.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;
import my.project.util.PortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "panel.app.order:Integer=20",
                "panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT
        },
        service = PanelApp.class
)
public class ProjectsAdminPanelApp extends BasePanelApp {

    @Reference(
            target = "(javax.portlet.name=" + PortletKeys.PROJECT_ADMIN_PORTLET + ")"
    )
    private Portlet portlet;

    @Override
    public Portlet getPortlet() {
        return portlet;
    }

    @Override
    public String getPortletId() {
        return PortletKeys.PROJECT_ADMIN_PORTLET;
    }

}
