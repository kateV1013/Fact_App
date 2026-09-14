package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.model.Categoria;
import ni.edu.uam.fact_app.util.DatosTemporales;

public class CategoriaController {

    private final ObservableList<Categoria> categorias = DatosTemporales.getCategorias();

    @FXML
    private TextField txtId;

    @FXML
    private CheckBox chkIdAutomatico;

    @FXML
    private TextField txtNombre;

    @FXML
    private CheckBox chkActiva;

    @FXML
    private TableView<Categoria> tblCategorias;

    @FXML
    private TableColumn<Categoria, Integer> colId;

    @FXML
    private TableColumn<Categoria, String> colNombre;

    @FXML
    private TableColumn<Categoria, String> colActiva;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colActiva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isActiva() ? "Si" : "No"));

        tblCategorias.setItems(categorias);
        tblCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, categoria) -> cargarCategoria(categoria)
        );
        chkIdAutomatico.selectedProperty().addListener(
                (observable, anterior, automatico) -> actualizarModoId()
        );

        limpiarFormulario();
    }

    @FXML
    private void nuevo() {
        tblCategorias.getSelectionModel().clearSelection();
        limpiarFormulario();
    }

    @FXML
    private void guardar() {
        if (!formularioValido()) {
            return;
        }

        Integer id = obtenerId();
        if (buscarPorId(id) != null) {
            mostrarError("Ya existe una categoria con ese ID.");
            return;
        }

        categorias.add(new Categoria(id, txtNombre.getText().trim(), chkActiva.isSelected()));
        limpiarFormulario();
        mostrarInformacion("Categoria guardada temporalmente.");
    }

    @FXML
    private void actualizar() {
        Categoria seleccionada = tblCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Seleccione una categoria para actualizar.");
            return;
        }

        if (!formularioValido()) {
            return;
        }

        Integer id = obtenerId();
        Categoria existente = buscarPorId(id);
        if (existente != null && existente != seleccionada) {
            mostrarError("Ya existe una categoria con ese ID.");
            return;
        }

        seleccionada.setId(id);
        seleccionada.setNombre(txtNombre.getText().trim());
        seleccionada.setActiva(chkActiva.isSelected());
        tblCategorias.refresh();
        limpiarFormulario();
        mostrarInformacion("Categoria actualizada.");
    }

    @FXML
    private void eliminar() {
        Categoria seleccionada = tblCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Seleccione una categoria para eliminar.");
            return;
        }

        categorias.remove(seleccionada);
        limpiarFormulario();
        mostrarInformacion("Categoria eliminada.");
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void cargarCategoria(Categoria categoria) {
        if (categoria == null) {
            return;
        }

        txtId.setText(String.valueOf(categoria.getId()));
        txtNombre.setText(categoria.getNombre());
        chkActiva.setSelected(categoria.isActiva());
        chkIdAutomatico.setSelected(false);
        actualizarModoId();
    }

    private Categoria buscarPorId(Integer id) {
        return categorias.stream()
                .filter(categoria -> categoria.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private boolean formularioValido() {
        if ((!chkIdAutomatico.isSelected() && txtId.getText().isBlank()) || txtNombre.getText().isBlank()) {
            mostrarError("Complete todos los campos obligatorios.");
            return false;
        }

        if (!chkIdAutomatico.isSelected()) {
            try {
                Integer.parseInt(txtId.getText().trim());
            } catch (NumberFormatException e) {
                mostrarError("El ID debe ser un numero entero.");
                return false;
            }
        }

        return true;
    }

    private void limpiarFormulario() {
        tblCategorias.getSelectionModel().clearSelection();
        chkIdAutomatico.setSelected(true);
        actualizarModoId();
        txtNombre.clear();
        chkActiva.setSelected(true);
    }

    private Integer obtenerId() {
        if (chkIdAutomatico.isSelected()) {
            return DatosTemporales.siguienteIdCategoria();
        }

        return Integer.parseInt(txtId.getText().trim());
    }

    private void actualizarModoId() {
        boolean automatico = chkIdAutomatico.isSelected();
        txtId.setDisable(automatico);
        if (automatico) {
            txtId.setText(String.valueOf(DatosTemporales.siguienteIdCategoria()));
        } else if (tblCategorias.getSelectionModel().getSelectedItem() == null) {
            txtId.clear();
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Categorias");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Categorias");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
