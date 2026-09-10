package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ni.edu.uam.fact_app.model.Cargo;

public class CargoController {

    private final ObservableList<Cargo> cargos = FXCollections.observableArrayList();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TableView<Cargo> tblCargos;

    @FXML
    private TableColumn<Cargo, Integer> colId;

    @FXML
    private TableColumn<Cargo, String> colNombre;

    @FXML
    private TableColumn<Cargo, String> colDescripcion;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

        tblCargos.setItems(cargos);
        tblCargos.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, cargo) -> cargarCargo(cargo)
        );

        limpiarFormulario();
    }

    @FXML
    private void nuevo() {
        tblCargos.getSelectionModel().clearSelection();
        limpiarFormulario();
    }

    @FXML
    private void guardar() {
        if (!formularioValido()) {
            return;
        }

        Integer id = Integer.parseInt(txtId.getText().trim());
        if (buscarPorId(id) != null) {
            mostrarError("Ya existe un cargo con ese ID.");
            return;
        }

        cargos.add(new Cargo(id, txtNombre.getText().trim(), txtDescripcion.getText().trim()));
        limpiarFormulario();
        mostrarInformacion("Cargo guardado temporalmente.");
    }

    @FXML
    private void actualizar() {
        Cargo seleccionado = tblCargos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un cargo para actualizar.");
            return;
        }

        if (!formularioValido()) {
            return;
        }

        Integer id = Integer.parseInt(txtId.getText().trim());
        Cargo existente = buscarPorId(id);
        if (existente != null && existente != seleccionado) {
            mostrarError("Ya existe un cargo con ese ID.");
            return;
        }

        seleccionado.setId(id);
        seleccionado.setNombre(txtNombre.getText().trim());
        seleccionado.setDescripcion(txtDescripcion.getText().trim());
        tblCargos.refresh();
        mostrarInformacion("Cargo actualizado.");
    }

    @FXML
    private void eliminar() {
        Cargo seleccionado = tblCargos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un cargo para eliminar.");
            return;
        }

        cargos.remove(seleccionado);
        limpiarFormulario();
        mostrarInformacion("Cargo eliminado.");
    }

    private void cargarCargo(Cargo cargo) {
        if (cargo == null) {
            return;
        }

        txtId.setText(String.valueOf(cargo.getId()));
        txtNombre.setText(cargo.getNombre());
        txtDescripcion.setText(cargo.getDescripcion());
    }

    private Cargo buscarPorId(Integer id) {
        return cargos.stream()
                .filter(cargo -> cargo.getId().equals(id))
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
        txtDescripcion.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cargos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
