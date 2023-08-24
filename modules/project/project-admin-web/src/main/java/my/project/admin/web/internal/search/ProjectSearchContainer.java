package my.project.admin.web.internal.search;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import my.project.model.Project;
import my.project.service.ProjectLocalServiceUtil;
import my.project.util.search.OrderByContext;

public class ProjectSearchContainer extends SearchContainer<Project> {

    private static final Log LOGGER = LogFactoryUtil.getLog(ProjectSearchContainer.class);

    private static final String NON_SEARCH_DEFAULT_FIELD = "name";
    private static final String SEARCH_DEFAULT_FIELD = "relevance";

    private static final Map<String, String> STATUS_MAP = new LinkedHashMap<>();

    static {
        STATUS_MAP.put(String.valueOf(WorkflowConstants.STATUS_ANY), "all");
        STATUS_MAP.put(String.valueOf(WorkflowConstants.STATUS_APPROVED), "active");
        STATUS_MAP.put(String.valueOf(WorkflowConstants.STATUS_INACTIVE),
                WorkflowConstants.getStatusLabel(WorkflowConstants.STATUS_INACTIVE)
        );
    }

    public ProjectSearchContainer(PortletRequest portletRequest, PortletURL iteratorURL) {
        super(portletRequest, null, null, "cur2", DEFAULT_DELTA,
                iteratorURL, null, "no-projects-were-found");

        OrderByContext orderByContext = getOrderByContext(portletRequest);
        SearchContext searchContext = getSearchContext(portletRequest, orderByContext);

        searchContext.setStart(getStart());
        searchContext.setEnd(getEnd());

        Map<String, Serializable> attributes = searchContext.getAttributes();

        processStatus(attributes);

        setOrderByCol(orderByContext.getOrderByCol());
        setOrderByType(orderByContext.getOrderByType());

        Hits hits = search(searchContext);

        List<Project> projectList = TransformUtil.transform(
                SearchResultUtil.getSearchResults(
                        hits, Locale.US),
                searchResult -> ProjectLocalServiceUtil.getProject(
                        searchResult.getClassPK()));

        setResultsAndTotal(() -> projectList, hits.getLength());
    }

    public static SearchContext getSearchContext(PortletRequest portletRequest, OrderByContext orderByContext) {

        HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
        SearchContext searchContext = SearchContextFactory.getInstance(httpServletRequest);

        String orderByType = orderByContext.getOrderByType();
        String sortKey = orderByContext.getSortKey();
        Integer sortType = orderByContext.getSortType();

        if (sortType != null && orderByType != null) {
            boolean reverse = orderByType.equals("desc");
            Sort sort = SortFactoryUtil.create(sortKey, sortType, reverse);
            searchContext.setSorts(sort);
        }

        return searchContext;
    }

    public static OrderByContext getOrderByContext(PortletRequest portletRequest) {

        OrderByContext orderByContext;

        String orderByCol = getAndValidateOrderByCol(portletRequest);
        String orderByType = ParamUtil.getString(portletRequest, "orderByType");

        switch (orderByCol) {
            case "name": {
                orderByType = getOrderByType(orderByType, "asc");
                orderByContext = new OrderByContext(orderByCol, orderByType, "title_String_sortable", Sort.STRING_TYPE);
                break;
            }
            case "status": {
                orderByType = getOrderByType(orderByType, "desc");
                orderByContext = new OrderByContext(orderByCol, orderByType, "status_sortable", Sort.INT_TYPE);
                break;
            }
            default: {
                orderByContext = new OrderByContext("relevance", null, null, null);
            }
        }

        return orderByContext;
    }

    private static String getOrderByType(String orderByType, String defaultOrder) {
        String alternativeOrder = (defaultOrder.equals("asc")) ? "desc" : "asc";
        if (orderByType.isEmpty() || orderByType.equals(defaultOrder)) {
            return defaultOrder;
        } else {
            return alternativeOrder;
        }
    }

    private static String getAndValidateOrderByCol(PortletRequest portletRequest) {
        String keywords = ParamUtil.getString(portletRequest, "keywords");

        String orderByDefaultValue = keywords.isEmpty() ? NON_SEARCH_DEFAULT_FIELD : SEARCH_DEFAULT_FIELD;

        String orderByCol = ParamUtil.getString(portletRequest, "orderByCol", orderByDefaultValue);

        if (keywords.isEmpty() && orderByCol.equals(SEARCH_DEFAULT_FIELD)) {
            return NON_SEARCH_DEFAULT_FIELD;
        } else {
            return orderByCol;
        }
    }

    private Hits search(SearchContext searchContext) {

        try {
            Indexer<Project> indexer = IndexerRegistryUtil.nullSafeGetIndexer(Project.class);
            return indexer.search(searchContext);

        } catch (SearchException e) {
            LOGGER.error(e);
        }

        return null;
    }

    private void processStatus(Map<String, Serializable> attributes) {
        String status = ParamUtil.getString(getPortletRequest(), "status");
        if (getStatusMap().containsKey(status)) {
            attributes.put("status", status);
        }
    }

    public Map<String, String> getStatusMap() {
        return STATUS_MAP;
    }

}
