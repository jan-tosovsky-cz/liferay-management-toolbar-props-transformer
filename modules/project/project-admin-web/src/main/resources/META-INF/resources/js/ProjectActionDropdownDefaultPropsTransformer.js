import {openSimpleInputModal} from 'frontend-js-web';

import openDeleteProjectModal from './openDeleteProjectModal';

const ACTIONS = {
    deleteProject(itemData) {
        openDeleteProjectModal({
            onDelete: () => {
                submitForm(document.hrefFm, itemData.deleteProjectURL);
            },
        });
    },

    updateProjectStatus(itemData) {
        submitForm(document.hrefFm, itemData.updateProjectStatusURL);
    },

    renameProject(itemData, namespace) {
        openSimpleInputModal({
            dialogTitle: Liferay.Language.get('rename-project'),
            formSubmitURL: itemData.renameProjectURL,
            idFieldName: 'id',
            idFieldValue: itemData.idFieldValue,
            mainFieldLabel: Liferay.Language.get('name'),
            mainFieldName: 'name',
            mainFieldPlaceholder: Liferay.Language.get('name'),
            mainFieldValue: itemData.mainFieldValue,
            namespace,
        });
    },
};

export default function propsTransformer({
                                             actions,
                                             items,
                                             portletNamespace,
                                             ...props
                                         }) {
    const updateItem = (item) => {
        const newItem = {
            ...item,
            onClick(event) {
                const action = item.data?.action;

                if (action) {
                    event.preventDefault();

                    ACTIONS[action]?.(item.data, portletNamespace);
                }
            },
        };

        if (Array.isArray(item.items)) {
            newItem.items = item.items.map(updateItem);
        }

        return newItem;
    };

    return {
        ...props,
        actions: actions?.map(updateItem),
        items: items?.map(updateItem),
    };
}
