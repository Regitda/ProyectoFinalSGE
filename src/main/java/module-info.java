module com.Project  {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.Project to javafx.fxml;
    exports com.Project;
    exports com.Project.Data;
    opens com.Project.Data to javafx.fxml;
    exports com.Project.Utils;
    opens com.Project.Utils to javafx.fxml;
    exports com.Project.Controllers;
    opens com.Project.Controllers to javafx.fxml;
}