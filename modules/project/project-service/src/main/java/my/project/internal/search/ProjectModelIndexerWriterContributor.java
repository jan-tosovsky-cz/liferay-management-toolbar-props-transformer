package my.project.internal.search;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.batch.BatchIndexingActionable;
import com.liferay.portal.search.batch.DynamicQueryBatchIndexingActionableFactory;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.index.contributor.helper.ModelIndexerWriterDocumentHelper;
import my.project.model.Project;
import my.project.service.ProjectLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "indexer.class.name=my.project.model.Project",
        service = ModelIndexerWriterContributor.class
)
public class ProjectModelIndexerWriterContributor implements ModelIndexerWriterContributor<Project> {

    @Reference
    private DynamicQueryBatchIndexingActionableFactory dynamicQueryBatchIndexingActionableFactory;

    @Reference
    private ProjectLocalService projectLocalService;

    @Override
    public void customize(
            BatchIndexingActionable batchIndexingActionable,
            ModelIndexerWriterDocumentHelper modelIndexerWriterDocumentHelper) {

        batchIndexingActionable.setPerformActionMethod((Project project) -> {
            Document document = modelIndexerWriterDocumentHelper.getDocument(project);
            batchIndexingActionable.addDocuments(document);
        });
    }

    @Override
    public BatchIndexingActionable getBatchIndexingActionable() {
        return dynamicQueryBatchIndexingActionableFactory.getBatchIndexingActionable(
                projectLocalService.getIndexableActionableDynamicQuery());
    }

    @Override
    public long getCompanyId(Project project) {
        return project.getCompanyId();
    }

}
