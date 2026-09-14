package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import ni.edu.uam.fact_app.util.DatosTemporales;

import java.io.File;
import java.math.BigDecimal;

public class ProductoController {

    private final ObservableList<Producto> productos = DatosTemporales.getProductos();
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
    private TableColumn<Producto, String> colActivo;

    @FXML
    private void initialize() {
        cmbCategoria.setItems(DatosTemporales.getCategorias());

        colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCategoria.setCellValueFactory(cellData -> {
            Categoria categoria = cellData.getValue().getCategoria();
            return new SimpleStringProperty(categoria == null ? "" : categoria.getNombre());
        });
        colPrecio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrecioVenta()));
        colExistencia.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getExistencia()));
        colActivo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isActivo() ? "Si" : "No"));

        tblProductos.setItems(productos);
        tblProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, producto) -> cargarProducto(producto)
        );
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

        if (buscarPorCodigo(txtCodigo.getText().trim()) != null) {
            mostrarError("Ya existe un producto con ese codigo.");
            return;
        }

        Producto producto = new Producto(
                DatosTemporales.siguienteIdProducto(),
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
        mostrarInformacion("Producto guardado temporalmente.");
    }

    @FXML
    private void actualizar() {
        Producto seleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un producto para actualizar.");
            return;
        }

        if (!formularioValido()) {
            return;
        }

        Producto existente = buscarPorCodigo(txtCodigo.getText().trim());
        if (existente != null && existente != seleccionado) {
            mostrarError("Ya existe un producto con ese codigo.");
            return;
        }

        seleccionado.setCodigo(txtCodigo.getText().trim());
        seleccionado.setNombre(txtNombre.getText().trim());
        seleccionado.setCategoria(cmbCategoria.getValue());
        seleccionado.setPrecioVenta(new BigDecimal(txtPrecio.getText().trim()));
        seleccionado.setExistencia(Integer.parseInt(txtExistencia.getText().trim()));
        seleccionado.setRutaImagen(rutaImagenSeleccionada);
        seleccionado.setActivo(chkActivo.isSelected());
        tblProductos.refresh();
        limpiarFormulario();
        mostrarInformacion("Producto actualizado.");
    }

    @FXML
    private void eliminar() {
        Producto seleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un producto para eliminar.");
            return;
        }

        productos.remove(seleccionado);
        limpiarFormulario();
        mostrarInformacion("Producto eliminado.");
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
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarError("El precio debe ser mayor que cero.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El precio en cordobas debe ser un numero valido.");
            return false;
        }

        try {
            int existencia = Integer.parseInt(txtExistencia.getText().trim());
            if (existencia < 0) {
                mostrarError("La existencia no puede ser negativa.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La existencia debe ser un numero entero.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        tblProductos.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtExistencia.clear();
        chkActivo.setSelected(true);
        imgProducto.setImage(null);
        rutaImagenSeleccionada = null;
    }

    private void cargarProducto(Producto producto) {
        if (producto == null) {
            return;
        }

        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecioVenta().toString());
        txtExistencia.setText(String.valueOf(producto.getExistencia()));
        chkActivo.setSelected(producto.isActivo());
        rutaImagenSeleccionada = producto.getRutaImagen();

        if (rutaImagenSeleccionada == null || rutaImagenSeleccionada.isBlank()) {
            imgProducto.setImage(null);
        } else {
            imgProducto.setImage(new Image(new File(rutaImagenSeleccionada).toURI().toString()));
        }
    }

    private Producto buscarPorCodigo(String codigo) {
        return productos.stream()
                .filter(producto -> producto.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Productos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Productos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
