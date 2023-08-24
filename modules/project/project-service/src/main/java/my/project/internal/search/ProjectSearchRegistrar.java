package my.project.internal.search;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchRegistrarHelper;
import my.project.model.Project;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = {})
public class ProjectSearchRegistrar {

    @Reference(target = "(indexer.class.name=my.project.model.Project)")
    private ModelIndexerWriterContributor<Project> modelIndexWriterContributor;

    @Reference
    private ModelSearchRegistrarHelper modelSearchRegistrarHelper;

    private ServiceRegistration<?> serviceRegistration;

    @Activate
    protected void activate(BundleContext bundleContext) {

        serviceRegistration = modelSearchRegistrarHelper.register(
                Project.class, bundleContext, modelSearchDefinition -> {
                    modelSearchDefinition.setDefaultSelectedFieldNames(
                            Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK);
                    modelSearchDefinition.setModelIndexWriteContributor(modelIndexWriterContributor);
                });
    }

    @Deactivate
    protected void deactivate() {
        serviceRegistration.unregister();
    }

}
