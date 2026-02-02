package controller.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import client.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.sceneNavigator.NavegadorVentanas;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.dto.auth.LoginDTO;
import shared.dto.auth.RegistroDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorLogin extends ControladorFuncionesCompartidas {

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

    @FXML private Button btnRegistroPrin;
    @FXML private Button btnLoginPrin;
    @FXML private Button btnAceptarLogin;
    @FXML private Button btnIrRegistro;
    @FXML private Button btnCambioAvatar;
    @FXML private Button btnRegistrarse;
    @FXML private Button btnVolverLogin;

    private byte[] avatarBytesSeleccionado;

    @FXML
    private void initialize() {
        System.out.println("LOGIN stylesheets = " + rootLogin.getStylesheets());
        System.out.println("LOGIN styleClass  = " + rootLogin.getStyleClass());
        ventanaInicial();

        btnLoginPrin.setOnAction(e -> mostrarLogin());
        btnRegistroPrin.setOnAction(e -> mostrarRegistro());
        btnIrRegistro.setOnAction(e -> mostrarRegistro());
        btnVolverLogin.setOnAction(e -> mostrarLogin());

        btnAceptarLogin.setOnAction(e -> onAceptarLogin());
        btnRegistrarse.setOnAction(e -> onRegistrarse());
        btnCambioAvatar.setOnAction(e -> onElegirAvatar());
    }

    private void mostrarLogin() {
        animarCambio(paneLogin, paneRegistro);
    }

    private void mostrarRegistro() {
        animarCambio(paneRegistro, paneLogin);
    }

    //aparece mostrando el registro
    private void ventanaInicial() {
        paneLogin.setOpacity(1);
        paneLogin.setVisible(true);
        paneLogin.setManaged(true);

        paneRegistro.setOpacity(0);
        paneRegistro.setVisible(false);
        paneRegistro.setManaged(false);
    }

    //animacion de cambio entre login y registro
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

        new ParallelTransition(fadeOut, fadeIn).play();
    }

    // ====== ELEGIR AVATAR ======
    private void onElegirAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(rootLogin.getScene().getWindow());
        if (archivo == null) return;

        try {
            avatarBytesSeleccionado = Files.readAllBytes(archivo.toPath());
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Avatar", "No se pudo leer la imagen: " + e.getMessage());
        }
    }

    // ====== LOGEARSE ======
    private void onAceptarLogin() {
        String usuario = usuarioLogin.getText() != null ? usuarioLogin.getText().trim() : "";
        String pass = contraseñaLogin.getText() != null ? contraseñaLogin.getText().trim() : "";

        if (usuario.isBlank() || pass.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Rellena usuario y contraseña.");
            return;
        }

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion("LOGIN", new LoginDTO(usuario, pass)));

                Platform.runLater(() -> {
                    if (!resp.ok) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Login", resp.message);
                        return;
                    }

                    if (!(resp.data instanceof UserDTO user)) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Login", "Respuesta inválida del servidor (sin usuario).");
                        return;
                    }

                    //para guardar datos de inicio de sesion
                    Sesion.setUsuario(user);

                    try {
                        Stage stage = (Stage) rootLogin.getScene().getWindow();
                        NavegadorVentanas.navegar("/escenas/principal/VentanaPantallaPrincipal.fxml");
                        Scene sceneNueva = stage.getScene();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        mostrarAlerta(Alert.AlertType.ERROR, "Error",
                            "No se pudo abrir la pantalla principal: " + ex.getMessage());
                    }

                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage())
                );
            }
        }).start();
    }

    // ====== REGISTRARSE ======
    private void onRegistrarse() {
        String correoStr = correo.getText() != null ? correo.getText().trim() : "";
        String nombreStr = nombreUsuario.getText() != null ? nombreUsuario.getText().trim() : "";
        String contra1 = contraseñaRegistro.getText() != null ? contraseñaRegistro.getText().trim() : "";
        String contra2 = validarContraseña.getText() != null ? validarContraseña.getText().trim() : "";

        if (correoStr.isBlank() || nombreStr.isBlank() || contra1.isBlank() || contra2.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor, completa todos los campos del registro.");
            return;
        }

        if (!contra1.equals(contra2)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseñas no coinciden",
                    "La contraseña y la validación deben ser iguales.");
            return;
        }

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion(
                        "REGISTER",
                        new RegistroDTO(correoStr, nombreStr, contra1, avatarBytesSeleccionado)
                ));

                Platform.runLater(() -> {
                    if (resp.ok) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Registro", resp.message);
                        mostrarLogin();
                    } else {
                        mostrarAlerta(Alert.AlertType.ERROR, "Registro", resp.message);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage())
                );
            }
        }).start();
    }
}
