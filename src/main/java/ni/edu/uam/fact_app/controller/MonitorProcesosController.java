package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.WindowEvent;

public class MonitorProcesosController {

    @FXML
    private Label lblEstadoClientes;

    @FXML
    private Label lblEstadoProductos;

    @FXML
    private Label lblPorcentajeClientes;

    @FXML
    private Label lblPorcentajeProductos;

    @FXML
    private ProgressBar barraClientes;

    @FXML
    private ProgressBar barraProductos;

    @FXML
    private Button btnIniciarClientes;

    @FXML
    private Button btnDetenerClientes;

    @FXML
    private Button btnIniciarProductos;

    @FXML
    private Button btnDetenerProductos;

    @FXML
    private Button btnReiniciar;

    private Thread hiloClientes;
    private Thread hiloProductos;

    @FXML
    private void initialize() {
        Platform.runLater(() ->
                btnIniciarClientes.getScene().getWindow().addEventHandler(
                        WindowEvent.WINDOW_HIDDEN,
                        event -> detenerAlCerrar()
                )
        );
    }

    @FXML
    private void iniciarClientes() {
        if (estaEjecutando(hiloClientes)) {
            return;
        }

        prepararInicio(barraClientes, lblPorcentajeClientes, lblEstadoClientes,
                btnIniciarClientes, btnDetenerClientes, "Procesando clientes...");

        Runnable tareaClientes = crearTareaProcesamiento(
                100,
                80,
                barraClientes,
                lblPorcentajeClientes,
                lblEstadoClientes,
                "Clientes procesados",
                "Procesamiento de clientes detenido",
                btnIniciarClientes,
                btnDetenerClientes
        );

        hiloClientes = new Thread(tareaClientes);
        hiloClientes.setName("Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
    }

    @FXML
    private void detenerClientes() {
        detenerProceso(hiloClientes, btnDetenerClientes, lblEstadoClientes);
    }

    @FXML
    private void iniciarProductos() {
        if (estaEjecutando(hiloProductos)) {
            return;
        }

        prepararInicio(barraProductos, lblPorcentajeProductos, lblEstadoProductos,
                btnIniciarProductos, btnDetenerProductos, "Procesando productos...");

        Runnable tareaProductos = crearTareaProcesamiento(
                50,
                140,
                barraProductos,
                lblPorcentajeProductos,
                lblEstadoProductos,
                "Productos procesados",
                "Procesamiento de productos detenido",
                btnIniciarProductos,
                btnDetenerProductos
        );

        hiloProductos = new Thread(tareaProductos);
        hiloProductos.setName("Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
    }

    @FXML
    private void detenerProductos() {
        detenerProceso(hiloProductos, btnDetenerProductos, lblEstadoProductos);
    }

    @FXML
    private void reiniciarProcesos() {
        if (estaEjecutando(hiloClientes) || estaEjecutando(hiloProductos)) {
            return;
        }

        reiniciarProceso(barraClientes, lblPorcentajeClientes, lblEstadoClientes,
                btnIniciarClientes, btnDetenerClientes, "Clientes en espera");
        reiniciarProceso(barraProductos, lblPorcentajeProductos, lblEstadoProductos,
                btnIniciarProductos, btnDetenerProductos, "Productos en espera");
    }

    private Runnable crearTareaProcesamiento(
            int totalRegistros,
            long pausaMilisegundos,
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            String mensajeFinalizado,
            String mensajeDetenido,
            Button btnIniciar,
            Button btnDetener
    ) {
        return () -> {
            try {
                for (int registro = 1; registro <= totalRegistros; registro++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(pausaMilisegundos);
                    final int avance = registro;
                    final int porcentaje = (int) Math.round((avance * 100.0) / totalRegistros);

                    Platform.runLater(() -> {
                        barraProgreso.setProgress(avance / (double) totalRegistros);
                        lblPorcentaje.setText(porcentaje + "%");
                    });
                }

                Platform.runLater(() ->
                        finalizarProceso(lblEstado, btnIniciar, btnDetener, mensajeFinalizado)
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() ->
                        finalizarProceso(lblEstado, btnIniciar, btnDetener, mensajeDetenido)
                );
            }
        };
    }

    private void prepararInicio(
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensaje
    ) {
        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText(mensaje);
        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);
        btnReiniciar.setDisable(true);
    }

    private void detenerProceso(Thread hilo, Button btnDetener, Label lblEstado) {
        if (!estaEjecutando(hilo)) {
            return;
        }

        btnDetener.setDisable(true);
        lblEstado.setText("Solicitando detencion...");
        hilo.interrupt();
    }

    private void finalizarProceso(
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensaje
    ) {
        lblEstado.setText(mensaje);
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(!btnDetenerClientes.isDisable() || !btnDetenerProductos.isDisable());
    }

    private void reiniciarProceso(
            ProgressBar barraProgreso,
            Label lblPorcentaje,
            Label lblEstado,
            Button btnIniciar,
            Button btnDetener,
            String mensaje
    ) {
        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText(mensaje);
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(false);
    }

    private boolean estaEjecutando(Thread hilo) {
        return hilo != null && hilo.isAlive();
    }

    public void detenerAlCerrar() {
        if (estaEjecutando(hiloClientes)) {
            hiloClientes.interrupt();
        }

        if (estaEjecutando(hiloProductos)) {
            hiloProductos.interrupt();
        }
    }
}
