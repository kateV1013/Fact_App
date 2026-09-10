package ni.edu.uam.fact_app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;

import java.io.IOException;
import java.util.Objects;

public final class SceneManager {
    private static final String ICONO_APP = "/fact_app/icons/app-icon.png";

    private SceneManager() { }

    public static void abrirVentana(String recurso, String titulo) throws IOException {
        var url = SceneManager.class.getResource(recurso);
        if (url == null) throw new IOException("FXML no encontrado: " + recurso);

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.getIcons().add(new Image(Objects.requireNonNull(
                SceneManager.class.getResourceAsStream(ICONO_APP)
        )));
        stage.setScene(new Scene(new FXMLLoader(url).load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
