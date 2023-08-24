# PropTransformers

A PropTransformer is as interface between the Java code and JavaScript modules.
It comes in handy especially when a particular Java action requires additional interactivity, 
for example confirmation before deleting the item. PropTransformers can import any existing 
Liferay Javascript module, which helps to keep the visual uniformity with the rest of the portal.

## Things that must be done in your project
1. In `gradle.properties` set `liferay.workspace.node.package.manager=npm`.
2. Add `npmscripts.config.js` to your `-web` module. That used in this example imports all `frontend-js-web` modules.
3. Add `package.json` to your `-web` module. That used in this example has `frontend-js-web` dependency.
4. Into `build.gradle` in your `-web` module copy the `jar` section from this example. It ensures your JavaScript files are bundled properly.
5. Ensure all PropsTransformers have the correct syntax.
6. Ensure all PropTransformers are referenced properly.

### Caveat
PropTransformers is a non-public (i.e. not supported) technology intended for internal use solely 
for Liferay modules. This example is the result of many trials/errors. It is not guaranteed it will
work in newer Liferay versions.

## Running example within IntelliJ IDEA
1. Clone the repo.
2. Select the project in the Project palette.
3. From the context menu choose Liferay | InitBundle.
4. In Gradle palette choose modules | Tasks | build | deploy.
5. In the top bar choose liferay-management-toolbar-props-transformer-server.
6. Run or Debug the server.
7. Once the server is up, open http://localhost:8080/ and finish the initial configuration.

## Inspecting core functionality
1. Open the product menu.
2. Choose the Content & Data | Projects menu.

### Adding a new project using a popup
1. Click the (+) button to invoke the modal popup dialog with a single text input.
2. Enter your value and click the Save button.
3. Any validation errors (empty or non-unique names) are shown directly in the popup, no redirection needs to be handled.

### Displaying a confirm dialog when deleting projects
1. When action menu is invoked, only relevant actions are shown.
2. To be able to delete project it has to be deactivated first.
3. Before deleting the project the visually distinct dialog is shown and has to be confirmed.

### Immediate validations when renaming projects
1. Similarly to creating projects, also their renaming is accomplished in a modal dialog. 
2. Any validation errors (empty or non-unique names) are shown directly in the popup.
