import {openSimpleInputModal} from 'frontend-js-web';

import openDeleteProjectModal from './openDeleteProjectModal';

export default function propsTransformer({portletNamespace, ...otherProps}) {
    return {
        ...otherProps,
        onActionButtonClick(event, {item}) {
            if (item?.data?.action === 'deleteSelectedProjects') {
                openDeleteProjectModal({
                    multiple: true,
                    onDelete: () => {
                        const form = document.getElementById(
                            `${portletNamespace}fm`
                        );

                        if (form) {
                            submitForm(form);
                        }
                    },
                });
            }
        },
        onCreateButtonClick(event, {item}) {
            const data = item?.data;

            const action = data?.action;

            if (action === 'addProject') {
                openSimpleInputModal({
                    dialogTitle: Liferay.Language.get('add-project'),
                    formSubmitURL: data?.addProjectURL,
                    mainFieldLabel: Liferay.Language.get('name'),
                    mainFieldName: 'name',
                    mainFieldPlaceholder: Liferay.Language.get('name'),
                    namespace: portletNamespace,
                });
            }
        },
    };
}