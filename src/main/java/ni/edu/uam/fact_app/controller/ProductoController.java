package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.model.Categoria;
import ni.edu.uam.fact_app.model.Producto;

import java.io.File;
import java.math.BigDecimal;

public class ProductoController {

    private final ObservableList<Producto> productos = FXCollections.observableArrayList();
    private String rutaImagenSeleccionada;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtExistencia;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private ImageView imgProducto;

    @FXML
    private TableView<Producto> tblProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colExistencia;

    @FXML
    private TableColumn<Producto, Boolean> colActivo;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(FXCollections.observableArrayList(
                new Categoria(1, "Bebidas", true),
                new Categoria(2, "Alimentos", true),
                new Categoria(3, "Limpieza", true),
                new Categoria(4, "Tecnologia", true)
        ));

        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCategoria.setCellValueFactory(cellData -> {
            Categoria categoria = cellData.getValue().getCategoria();
            return new SimpleStringProperty(categoria == null ? "" : categoria.getNombre());
        });
        colPrecio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrecioVenta()));
        colExistencia.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getExistencia()));
        colActivo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isActivo()));

        tblProductos.setItems(productos);
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen del producto");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(stage);
        if (archivo != null) {
            rutaImagenSeleccionada = archivo.getAbsolutePath();
            imgProducto.setImage(new Image(archivo.toURI().toString()));
        }
    }

    @FXML
    private void guardar() {
        if (!formularioValido()) {
            return;
        }

        Producto producto = new Producto(
                productos.size() + 1,
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                cmbCategoria.getValue(),
                new BigDecimal(txtPrecio.getText().trim()),
                Integer.parseInt(txtExistencia.getText().trim()),
                rutaImagenSeleccionada,
                chkActivo.isSelected()
        );

        productos.add(producto);
        limpiarFormulario();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Productos");
        alert.setHeaderText(null);
        alert.setContentText("Producto guardado temporalmente.");
        alert.showAndWait();
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean formularioValido() {
        if (txtCodigo.getText().isBlank()
                || txtNombre.getText().isBlank()
                || cmbCategoria.getValue() == null
                || txtPrecio.getText().isBlank()
                || txtExistencia.getText().isBlank()) {
            mostrarError("Complete todos los campos obligatorios.");
            return false;
        }

        try {
            new BigDecimal(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un numero valido.");
            return false;
        }

        try {
            Integer.parseInt(txtExistencia.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("La existencia debe ser un numero entero.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtExistencia.clear();
        chkActivo.setSelected(true);
        imgProducto.setImage(null);
        rutaImagenSeleccionada = null;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Productos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
