package controller.main.friends.components;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import shared.dto.friends.AmigoDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorTarjetaAmigos extends ControladorFuncionesCompartidas {

    @FXML private Label nombreAmigo;
    @FXML private Button btnVerDiario;
    @FXML private Button btnEliminar;

    private String nombreUsuario;

    public void setDatos(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        nombreAmigo.setText(nombreUsuario);
    }

    @FXML
    private void initialize() {
        btnVerDiario.setOnAction(e -> verDiarioAmigo());
        btnEliminar.setOnAction(e -> eliminarAmigo());
    }

    // VER DIARIO DE AMIGO (pendiente)
    private void verDiarioAmigo() {
        // Aquí navegarías a la vista del diario de ese usuario
        // Ej: cambiarEscena("DiarioAmigo.fxml", ...), etc.
    }

    // ELIMINAR AMIGO
    private void eliminarAmigo() {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Nombre de amigo inválido.");
            return;
        }

        btnEliminar.setDisable(true);

        new Thread(() -> {
            try {

                Respuesta resp = Sesion.tcp.enviar(new Peticion("ELIMINAR_AMIGO", new AmigoDTO(nombreUsuario)));

                Platform.runLater(() -> {
                    try {
                        if (resp != null && resp.ok) {

                        	Node tarjeta = nombreAmigo.getParent();
                            if (tarjeta != null && tarjeta.getParent() instanceof Pane pane) {
                                pane.getChildren().remove(tarjeta);
                            }

                            mostrarAlerta(Alert.AlertType.INFORMATION, "OK", resp.message != null ? resp.message : "Amigo eliminado.");
                        } else {
                            String msg = (resp == null) ? "Respuesta nula del servidor." : (resp.message != null ? resp.message : "Error eliminando amigo.");
                            mostrarAlerta(Alert.AlertType.ERROR, "Error", msg);
                            btnEliminar.setDisable(false);
                        }
                    } catch (Exception uiEx) {
                        btnEliminar.setDisable(false);
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error actualizando la UI: " + uiEx.getMessage());
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnEliminar.setDisable(false);
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage());
                });
            }
        }, "eliminar-amigo").start();
    }
}
