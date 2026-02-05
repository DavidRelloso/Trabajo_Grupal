package controller.main.friends.components;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.diary.ControladorDiarioPersonal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import shared.dto.friends.AmigoDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorTarjetaAmigos extends ControladorFuncionesCompartidas {

	@FXML private HBox rootTarjetaAmigo;
	@FXML private StackPane rootListaAmigos;
	
    @FXML private Label nombreAmigo;
    @FXML private Button btnVerDiario;
    @FXML private Button btnEliminar;

    private String nombreUsuario;

    public void setDatos(String nombreUsuario, StackPane rootListaAmigos) {
        this.nombreUsuario = nombreUsuario;
        this.rootListaAmigos = rootListaAmigos;
        nombreAmigo.setText(nombreUsuario);
    }

    @FXML
    private void initialize() {
        btnVerDiario.setOnAction(e -> verDiarioAmigo());
        btnEliminar.setOnAction(e -> eliminarAmigo());
    }

    // VER DIARIO DE AMIGO
    private void verDiarioAmigo() {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Nombre de amigo inválido.");
            return;
        }

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/diario/VentanaDiarioPersonal.fxml"));
			Parent root = loader.load();

			ControladorDiarioPersonal ctrl = loader.getController();
			ctrl.setDiarioAmigo(nombreUsuario);
			ctrl.cargar();

			Stage stage = (Stage) btnVerDiario.getScene().getWindow();
			stage.getScene().setRoot(root);

		} catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir el diario: " + e.getMessage());
        }
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