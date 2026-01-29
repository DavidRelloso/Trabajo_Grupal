package controller;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UsuarioService;

import shared.UserDTO;
import shared.LoginDTO;
import shared.Peticion;
import shared.RegistroDTO;
import shared.Respuesta;

public class ControladorLogin {

	@FXML private StackPane rootLogin;
	@FXML private StackPane paneLogin;
	@FXML private StackPane paneRegistro;

	@FXML private TextField usuarioLogin;
	@FXML private PasswordField contraseñaLogin;

	@FXML private TextField correo;
	@FXML private TextField nombreUsuario;
	@FXML private PasswordField contraseñaRegistro;
	@FXML private PasswordField validarContraseña;
	@FXML private ImageView avatarUsuario;

	private byte[] avatarBytesSeleccionado;

	@SuppressWarnings("unused")
	private UsuarioService usuarioService = new UsuarioService();

	@FXML
	private void initialize() {
		ventanaInicial();
	}

	@FXML
	private void mostrarLogin() {
		animarCambio(paneLogin, paneRegistro);
	}

	@FXML
	private void mostrarRegistro() {
		animarCambio(paneRegistro, paneLogin);
	}

	private void ventanaInicial() {
		paneLogin.setOpacity(1);
		paneLogin.setVisible(true);
		paneLogin.setManaged(true);

		paneRegistro.setOpacity(0);
		paneRegistro.setVisible(false);
		paneRegistro.setManaged(false);
	}

	private void animarCambio(StackPane panelMostrar, StackPane panelOcultar) {

		if (panelMostrar.isVisible())
			return;

		panelMostrar.setVisible(true);
		panelMostrar.setManaged(true);
		panelMostrar.setOpacity(0);

		FadeTransition fadeOut = new FadeTransition(Duration.millis(250), panelOcultar);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);

		FadeTransition fadeIn = new FadeTransition(Duration.millis(250), panelMostrar);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);

		fadeOut.setOnFinished(e -> {
			panelOcultar.setVisible(false);
			panelOcultar.setManaged(false);
		});

		ParallelTransition transicion = new ParallelTransition(fadeOut, fadeIn);
		transicion.play();
	}

	@FXML
	private void onElegirAvatar() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar archivo");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
		File archivo = fileChooser.showOpenDialog(rootLogin.getScene().getWindow());

		if (archivo != null) {
			System.out.println("Archivo seleccionado: " + archivo.getAbsolutePath());
			try {
				avatarBytesSeleccionado = Files.readAllBytes(archivo.toPath());
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void onAceptarLogin() {

		String usuario = usuarioLogin.getText() != null ? usuarioLogin.getText().trim() : "";
		String pass = contraseñaLogin.getText() != null ? contraseñaLogin.getText().trim() : "";

		if (usuario.isEmpty() || pass.isEmpty()) {
			mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Rellena usuario y contraseña.");
			return;
		}

		new Thread(() -> {
			try {
				Peticion req = new Peticion("LOGIN", new LoginDTO(usuario, pass));
				Respuesta resp = enviar(req);

				Platform.runLater(() -> {
					if (resp.ok) {
						try {
							UserDTO user = (UserDTO) resp.data;

							FXMLLoader loader = new FXMLLoader(
									getClass().getResource("/escenas/VentanaPantallaPrincipal.fxml"));
							Parent root = loader.load();

							ControladorPantallaPrincipal controller = loader.getController();
							controller.setUsuarioSesion(user);

							Stage stage = (Stage) rootLogin.getScene().getWindow();
							stage.setScene(new Scene(root));
							stage.show();

						} catch (Exception ex) {
							mostrarAlerta(Alert.AlertType.ERROR, "Error",
									"No se pudo abrir la pantalla principal: " + ex.getMessage());
						}
					} else {
						mostrarAlerta(Alert.AlertType.ERROR, "Login", resp.message);
					}
				});

			} catch (Exception e) {
				Platform.runLater(() -> mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage()));
			}
		}).start();
	}

	@FXML
	private void onRegistrarse() {

		String correoStr = correo.getText() != null ? correo.getText().trim() : "";
		String nombreStr = nombreUsuario.getText() != null ? nombreUsuario.getText().trim() : "";
		String contra1 = contraseñaRegistro.getText() != null ? contraseñaRegistro.getText().trim() : "";
		String contra2 = validarContraseña.getText() != null ? validarContraseña.getText().trim() : "";

		if (correoStr.isEmpty() || nombreStr.isEmpty() || contra1.isEmpty() || contra2.isEmpty()) {
			mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos",
					"Por favor, completa todos los campos del registro.");
			return;
		}

		if (!contra1.equals(contra2)) {
			mostrarAlerta(Alert.AlertType.ERROR, "Contraseñas no coinciden",
					"La contraseña y la validación de contraseña deben ser iguales.");
			return;
		}

		new Thread(() -> {
			try {
				Peticion req = new Peticion("REGISTER",
						new RegistroDTO(correoStr, nombreStr, contra1, avatarBytesSeleccionado));
				Respuesta resp = enviar(req);

				Platform.runLater(() -> {
					if (resp.ok) {
						mostrarAlerta(Alert.AlertType.INFORMATION, "Registro", resp.message);
						mostrarLogin();

					} else {
						mostrarAlerta(Alert.AlertType.ERROR, "Registro", resp.message);
					}
				});

			} catch (Exception e) {
				Platform.runLater(() -> mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage()));
			}
		}).start();

	}

	private Respuesta enviar(Peticion req) throws Exception {
		try (Socket s = new Socket("DAM2-24", 5000);
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {

			out.writeObject(req);
			out.flush();

			return (Respuesta) in.readObject();
		}
	}

	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

}
