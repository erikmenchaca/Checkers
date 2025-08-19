/**
 * Defines the module for the CheckersFX application.
 * This module requires the necessary JavaFX modules to run the GUI.
 */
module checkers {
    // This application requires the JavaFX controls module.
    requires javafx.controls;

    // This application requires the JavaFX graphics module for drawing shapes and handling scenes.
    requires javafx.graphics;

    // Exports the main package to the JavaFX graphics module,
    // allowing the JavaFX framework to launch the application.
     exports checkers.view;

    // Replace with an existing package, for example:
    // exports checkers; // Uncomment and update if 'checkers' package exists
}