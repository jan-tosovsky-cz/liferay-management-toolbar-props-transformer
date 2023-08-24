package my.project.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownGroupItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.util.List;
import java.util.Map;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import my.project.admin.web.internal.search.ProjectSearchContainer;

public class ProjectManagementToolbarDisplayContext extends SearchContainerManagementToolbarDisplayContext {

    private static final String SEARCH_ORDER_BY_KEY = "relevance";

    private final ProjectSearchContainer projectSearchContainer;

    public ProjectManagementToolbarDisplayContext(
            LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse,
            HttpServletRequest httpServletRequest,
            ProjectSearchContainer projectSearchContainer) {

        super(httpServletRequest, liferayPortletRequest, liferayPortletResponse, projectSearchContainer);

        this.projectSearchContainer = projectSearchContainer;
    }

    @Override
    public Boolean isSelectable() {
        return false;
    }

    @Override
    public String getSearchActionURL() {
        return PortletURLBuilder.create(getPortletURL())
                .setParameter(getOrderByColParam(), SEARCH_ORDER_BY_KEY)
                .buildString();
    }

    @Override
    public String getClearResultsURL() {
        return PortletURLBuilder.create(getPortletURL())
                .setKeywords("")
                .setParameter(getStatusParam(), WorkflowConstants.STATUS_ANY)
                .setParameter(getOrderByColParam(), getOrderByKeys()[0])
                .buildString();
    }

    @Override
    protected List<DropdownItem> getDropdownItems(Map<String, String> entriesMap, PortletURL entryURL, String parameterName, String parameterValue) {
        return entriesMap != null && !entriesMap.isEmpty() ? new DropdownItemList() {
            {
                String keywords = httpServletRequest.getParameter("keywords");

                for (Map.Entry<String, String> entry : entriesMap.entrySet()) {
                    this.add((dropdownItem) -> {
                        if (parameterValue != null) {
                            dropdownItem.setActive(parameterValue.equals(entry.getKey()));
                        }

                        dropdownItem.setHref(entryURL, parameterName, entry.getKey());
                        dropdownItem.setLabel(LanguageUtil.get(httpServletRequest, entry.getValue()));

                        if (entry.getKey().equals(SEARCH_ORDER_BY_KEY)) {
                            if (keywords == null || keywords.equals("")) {
                                dropdownItem.setDisabled(true);
                            }
                        }
                    });
                }
            }
        } : null;
    }

    @Override
    protected String[] getOrderByKeys() {
        return new String[]{"name", "status", SEARCH_ORDER_BY_KEY};
    }

    @Override
    public List<LabelItem> getFilterLabelItems() {
        LabelItemList labelItems = new LabelItemList();

        int status = getStatus();

        if (status != WorkflowConstants.STATUS_ANY) {
            labelItems.add(labelItem -> {
                PortletURL removeLabelURL = PortletURLBuilder.create(getPortletURL())
                        .setParameter(getStatusParam(), WorkflowConstants.STATUS_ANY)
                        .buildPortletURL();

                labelItem.putData("removeLabelURL", removeLabelURL.toString());
                labelItem.setLabel("Status: " + LanguageUtil.get(httpServletRequest, projectSearchContainer.getStatusMap().get(String.valueOf(status))));
                labelItem.setDismissible(true);
            });
        }

        return labelItems;
    }

    @Override
    public List<DropdownItem> getFilterDropdownItems() {

        List<DropdownItem> items = super.getFilterDropdownItems();

        List<DropdownItem> filterStatusDropdownItems = getDropdownItems(
                projectSearchContainer.getStatusMap(), getPortletURL(), getStatusParam(), String.valueOf(getStatus()));

        if (filterStatusDropdownItems != null) {
            items.add(0, new DropdownGroupItem() {{
                setDropdownItems(filterStatusDropdownItems);
                setLabel(LanguageUtil.get(httpServletRequest, "filter-by-status"));
            }});
        }

        return items;
    }

    @Override
    public Boolean isShowCreationMenu() {
        return true;
    }

    @Override
    public CreationMenu getCreationMenu() {

        ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

        return CreationMenuBuilder.addDropdownItem(
                dropdownItem -> {
                    dropdownItem.putData("action", "addProject");
                    dropdownItem.putData(
                            "addProjectURL",
                            PortletURLBuilder.createActionURL(
                                    liferayPortletResponse
                            ).setActionName(
                                    "/add-project"
                            ).setMVCPath(
                                    "/add-project.jsp"
                            ).setRedirect(
                                    themeDisplay.getURLCurrent()
                            ).buildString());
                    dropdownItem.setLabel(
                            LanguageUtil.get(httpServletRequest, "add"));
                }
        ).build();
    }

    private String getStatusParam() {
        return "status";
    }

    private int getStatus() {
        return ParamUtil.getInteger(liferayPortletRequest, getStatusParam(), WorkflowConstants.STATUS_ANY);
    }

}
