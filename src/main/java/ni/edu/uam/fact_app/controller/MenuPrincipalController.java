package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import ni.edu.uam.fact_app.util.DatosTemporales;
import ni.edu.uam.fact_app.util.SceneManager;

import java.io.IOException;
import java.math.RoundingMode;

public class MenuPrincipalController {

    @FXML
    private Label lblProductosActivos;

    @FXML
    private Label lblUnidadesInventario;

    @FXML
    private Label lblCategoriasRegistradas;

    @FXML
    private Label lblCargosRegistrados;

    @FXML
    private Label lblValorInventario;

    @FXML
    private void initialize() {
        actualizarResumen();
    }

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
        actualizarResumen();
    }

    @FXML
    private void salir() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Salir");
        confirmacion.setHeaderText("Confirmar salida");
        confirmacion.setContentText("¿Desea cerrar la aplicación?");

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        confirmacion.getButtonTypes().setAll(btnSi, btnNo);

        confirmacion.showAndWait()
                .filter(respuesta -> respuesta == btnSi)
                .ifPresent(respuesta -> Platform.exit());
    }

    private void actualizarResumen() {
        lblProductosActivos.setText(String.valueOf(DatosTemporales.totalProductosActivos()));
        lblUnidadesInventario.setText(String.valueOf(DatosTemporales.totalUnidadesInventario()));
        lblCategoriasRegistradas.setText(String.valueOf(DatosTemporales.totalCategoriasRegistradas()));
        lblCargosRegistrados.setText(String.valueOf(DatosTemporales.totalCargosRegistrados()));
        lblValorInventario.setText("C$ " + DatosTemporales.valorTotalInventario().setScale(2, RoundingMode.HALF_UP));
    }
}
