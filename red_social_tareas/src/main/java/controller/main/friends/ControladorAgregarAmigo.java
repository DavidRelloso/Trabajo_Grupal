package controller.main.friends;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import shared.dto.friends.AgregarAmigoDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorAgregarAmigo extends ControladorFuncionesCompartidas {

	@FXML TextField txtNombreAmigo;
	@FXML Button btnAgregar;

	@FXML
	private void initialize() {

	    btnAgregar.setOnAction(e -> enviarPeticionAmigo());
	    
	}

	// ENVIAR PETICION AMISTAD
	private void enviarPeticionAmigo() {

        String nombreUsuario = Sesion.getUsuario().nombreUsuario; 
		String nombreAmigo = txtNombreAmigo.getText() != null ? txtNombreAmigo.getText().trim() : "";

		if (nombreAmigo.isBlank()) {
			mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Rellena el nombre del usuario.");
			return;
		}

		new Thread(() -> {
			try {
				
				AgregarAmigoDTO dto = new AgregarAmigoDTO(nombreUsuario,nombreAmigo);

				Respuesta resp = enviar(new Peticion("AGREGAR_AMIGO", dto));

				Platform.runLater(() -> {
					if (!resp.ok) {
						mostrarAlerta(Alert.AlertType.ERROR, "Solicitud de amistad", resp.message);
						return;
					}

					mostrarAlerta(Alert.AlertType.INFORMATION, "Solicitud enviada",
							"La solicitud de amistad se ha enviado correctamente.");
				});

			} catch (Exception e) {
				Platform.runLater(() -> mostrarAlerta(Alert.AlertType.ERROR, "Error",
						"No se pudo conectar con el servidor: " + e.getMessage()));
			}
		}).start();
	}

}
