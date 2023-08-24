import {openModal, sub} from 'frontend-js-web';

export default function openDeleteProjectModal({
    multiple = false,
    onDelete,
}) {
    openModal({
        bodyHTML: Liferay.Language.get('are-you-sure-you-want-to-delete-this'),
        buttons: [
            {
                autoFocus: true,
                displayType: 'secondary',
                label: Liferay.Language.get('cancel'),
                type: 'cancel',
            },
            {
                displayType: 'danger',
                label: Liferay.Language.get('delete'),
                onClick: ({processClose}) => {
                    processClose();

                    onDelete();
                },
            },
        ],
        status: 'danger',
        title: sub(
            Liferay.Language.get('delete-x'),
            multiple
                ? Liferay.Language.get('projects')
                : Liferay.Language.get('project')
        ),
    });
}
