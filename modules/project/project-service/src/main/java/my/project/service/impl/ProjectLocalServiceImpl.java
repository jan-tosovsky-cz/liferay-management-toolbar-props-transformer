package my.project.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.util.Date;
import my.project.exception.DuplicateProjectNameException;
import my.project.exception.ProjectNameException;
import my.project.model.Project;
import my.project.service.base.ProjectLocalServiceBaseImpl;
import org.osgi.service.component.annotations.Component;

@Component(
	property = "model.class.name=my.project.model.Project",
	service = AopService.class
)
public class ProjectLocalServiceImpl extends ProjectLocalServiceBaseImpl {

	@Override
	public Project addProject(
			long userId, long groupId, String name, ServiceContext serviceContext)
			throws PortalException {

		validate(name);

		User user = userLocalService.getUser(userId);

		long projectId = counterLocalService.increment();

		Project project = projectPersistence.create(projectId);

		project.setGroupId(groupId);
		project.setCompanyId(user.getCompanyId());
		project.setUserId(userId);
		project.setUserName(user.getFullName());
		project.setName(name);
		project.setStatus(WorkflowConstants.STATUS_APPROVED);

		project = projectLocalService.updateProject(project);

		return project;
	}

	@Override
	public Project renameProject(
			long userId, long projectId, String name, ServiceContext serviceContext)
			throws PortalException {

		Project project = getProject(projectId);

		validate(name);

		User user = userLocalService.getUser(userId);

		project.setUserId(userId);
		project.setUserName(user.getFullName());
		project.setModifiedDate(
				serviceContext.getModifiedDate(new Date()));
		project.setName(name);

		return projectLocalService.updateProject(project);
	}

	@Override
	public Project updateProjectStatus(
			long userId, long projectId, int status, ServiceContext serviceContext)
			throws PortalException {

		Project project = getProject(projectId);

		User user = userLocalService.getUser(userId);

		project.setUserId(userId);
		project.setUserName(user.getFullName());
		project.setModifiedDate(
				serviceContext.getModifiedDate(new Date()));
		project.setStatus(status);

		return projectLocalService.updateProject(project);
	}

	@Override
	public Project deleteProject(Project project) throws PortalException {

		projectLocalService.deleteProject(project);

		return project;
	}

	@Override
	public Project fetchByName(String name) {
		return projectPersistence.fetchByName(name);
	}

	private void validate(String name) throws PortalException {
		if (Validator.isNull(name)) {
			throw new ProjectNameException();
		}

		int nameMaxLength = ModelHintsUtil.getMaxLength(Project.class.getName(), "name");

		if (name.length() > nameMaxLength) {
			throw new ProjectNameException("Maximum length of name exceeded");
		}

		Project project = projectPersistence.fetchByName(name);

		if (project != null) {
			throw new DuplicateProjectNameException(name);
		}
	}
}