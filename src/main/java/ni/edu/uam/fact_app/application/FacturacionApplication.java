package ni.edu.uam.fact_app.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FacturacionApplication extends Application {

    private static final String ICONO_APP = "/fact_app/icons/app-icon.png";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FacturacionApplication.class.getResource("/fact_app/fxml/menu-principal.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Fact App");
        stage.getIcons().add(new Image(Objects.requireNonNull(
                FacturacionApplication.class.getResourceAsStream(ICONO_APP)
        )));
        stage.setScene(scene);
        stage.show();
    }
}
