package controller;

import java.io.File;
import java.io.IOException;

import entity.Usuario;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import service.UsuarioService;

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

        if (panelMostrar.isVisible()) return;

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
	            avatarBytesSeleccionado = java.nio.file.Files.readAllBytes(archivo.toPath());
			}catch (IOException e) {
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
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Por favor, rellena usuario y contraseña.");
            return;
        }

        System.out.println(usuario + " " + pass);
        
        Usuario u = usuarioService.findByNombreAndContra(usuario, pass);

        System.out.println(u);
        
        if (u != null) {
			mostrarAlerta(Alert.AlertType.INFORMATION,
					"Login correcto",
					"Bienvenido, " + u.getNombre_usuario() + "!");
		} else {
			mostrarAlerta(Alert.AlertType.ERROR,
					"Error de login",
					"Usuario o contraseña incorrectos.");
		}
    }

    @FXML
    private void onRegistrarse() {
        String c = correo.getText() != null ? correo.getText().trim() : "";
        String n = nombreUsuario.getText() != null ? nombreUsuario.getText().trim() : "";
        String pass1 = contraseñaRegistro.getText() != null ? contraseñaRegistro.getText().trim() : "";
        String pass2 = validarContraseña.getText() != null ? validarContraseña.getText().trim() : "";

        if (c.isEmpty() || n.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Por favor, completa todos los campos del registro.");
            return;
        }

        if (!pass1.equals(pass2)) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Contraseñas no coinciden",
                    "La contraseña y la validación de contraseña deben ser iguales.");
            return;
        }
        
        if (avatarUsuario.getImage() == null) {
			mostrarAlerta(Alert.AlertType.WARNING,
					"Avatar no seleccionado",
					"Por favor, selecciona un avatar para tu perfil.");
			return;
		}
        
        usuarioService.save(new Usuario(n, c, pass1, avatarBytesSeleccionado));
       
        mostrarAlerta(Alert.AlertType.INFORMATION,
                "Registro correcto",
                "Registro realizado correctamente.");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
