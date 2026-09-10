package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ni.edu.uam.fact_app.model.Categoria;

public class CategoriaController {

    private final ObservableList<Categoria> categorias = FXCollections.observableArrayList();

    @FXML
    private TextField txtId;

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
    private TableColumn<Categoria, Boolean> colActiva;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colActiva.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isActiva()));

        tblCategorias.setItems(categorias);
        tblCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, categoria) -> cargarCategoria(categoria)
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

        Integer id = Integer.parseInt(txtId.getText().trim());
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

        Integer id = Integer.parseInt(txtId.getText().trim());
        Categoria existente = buscarPorId(id);
        if (existente != null && existente != seleccionada) {
            mostrarError("Ya existe una categoria con ese ID.");
            return;
        }

        seleccionada.setId(id);
        seleccionada.setNombre(txtNombre.getText().trim());
        seleccionada.setActiva(chkActiva.isSelected());
        tblCategorias.refresh();
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

    private void cargarCategoria(Categoria categoria) {
        if (categoria == null) {
            return;
        }

        txtId.setText(String.valueOf(categoria.getId()));
        txtNombre.setText(categoria.getNombre());
        chkActiva.setSelected(categoria.isActiva());
    }

    private Categoria buscarPorId(Integer id) {
        return categorias.stream()
                .filter(categoria -> categoria.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private boolean formularioValido() {
        if (txtId.getText().isBlank() || txtNombre.getText().isBlank()) {
            mostrarError("Complete todos los campos obligatorios.");
            return false;
        }

        try {
            Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("El ID debe ser un numero entero.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        chkActiva.setSelected(true);
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
