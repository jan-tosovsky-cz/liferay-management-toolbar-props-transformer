package my.project.internal.search;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import my.project.model.Project;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = "indexer.class.name=my.project.model.Project",
        service = ModelDocumentContributor.class
)
public class ProjectModelDocumentContributor implements ModelDocumentContributor<Project> {

    @Override
    public void contribute(Document document, Project project) {
        document.addTextSortable(Field.TITLE, project.getName());
        document.addNumberSortable(Field.STATUS, project.getStatus());
        document.addDateSortable(Field.MODIFIED_DATE, project.getModifiedDate());
        document.addDateSortable(Field.CREATE_DATE, project.getCreateDate());
    }

}
