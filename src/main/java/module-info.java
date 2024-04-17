module coinCompanion.app {
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires java.desktop;

  opens coinCompanion.app to javafx.fxml;
  opens coinCompanion.objects to javafx.base;

  exports coinCompanion.app;
  exports coinCompanion.objects;
  exports coinCompanion.enums;
}