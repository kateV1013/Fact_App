package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import ni.edu.uam.fact_app.util.SceneManager;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private void abrirCategorias() {
        abrirVentana("/fact_app/fxml/categoria-view.fxml", "Categorias", "categorias");
    }

    @FXML
    private void abrirCargos() {
        abrirVentana("/fact_app/fxml/cargo-view.fxml", "Cargos", "cargos");
    }

    @FXML
    private void abrirProductos() {
        abrirVentana("/fact_app/fxml/producto-view.fxml", "Productos", "productos");
    }

    private void abrirVentana(String recurso, String titulo, String nombreVentana) {
        try {
            SceneManager.abrirVentana(recurso, titulo);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana de " + nombreVentana);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void salir() {
        Platform.exit();
    }
}
