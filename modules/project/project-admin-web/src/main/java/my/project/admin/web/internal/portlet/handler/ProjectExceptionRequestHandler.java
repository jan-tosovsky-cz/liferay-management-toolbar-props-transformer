package my.project.admin.web.internal.portlet.handler;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import my.project.exception.DuplicateProjectNameException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ProjectExceptionRequestHandler.class)
public class ProjectExceptionRequestHandler {

    private static final Log LOGGER = LogFactoryUtil.getLog(ProjectExceptionRequestHandler.class);

    @Reference
    private Language language;

    public void handlePortalException(
            ActionRequest actionRequest, ActionResponse actionResponse,
            PortalException portalException)
            throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(portalException);
        }

        ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String errorMessage = "an-unexpected-error-occurred";

        if (portalException instanceof DuplicateProjectNameException) {
            errorMessage = "please-enter-a-unique-name";
        } else {
            LOGGER.error(portalException);
        }

        JSONObject jsonObject = JSONUtil.put(
                "error", language.get(themeDisplay.getLocale(), errorMessage));

        JSONPortletResponseUtil.writeJSON(
                actionRequest, actionResponse, jsonObject);
    }

}
