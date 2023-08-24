package my.project.internal.search;

import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.query.contributor.ModelPreFilterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchSettings;
import java.io.Serializable;
import java.util.Map;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = "indexer.class.name=my.project.model.Project",
        service = ModelPreFilterContributor.class
)
public class ProjectModelPreFilterContributor implements ModelPreFilterContributor {

    @Override
    public void contribute(BooleanFilter booleanFilter, ModelSearchSettings modelSearchSettings, SearchContext searchContext) {
        Map<String, Serializable> attributes = searchContext.getAttributes();
        if (attributes.containsKey("status")) {
            addStatusToFilter(booleanFilter, attributes);
        }
    }

    private void addStatusToFilter(BooleanFilter booleanFilter, Map<String, Serializable> attributes) {
        String status = (String) attributes.get("status");

        if (!status.equals(String.valueOf(WorkflowConstants.STATUS_ANY))) {
            TermsFilter termsFilter = new TermsFilter("status");
            termsFilter.addValues(status);
            booleanFilter.add(termsFilter, BooleanClauseOccur.MUST);
        }
    }

}
