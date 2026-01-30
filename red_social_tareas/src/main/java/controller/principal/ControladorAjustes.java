package controller.principal;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

import client.Sesion;
import controller.ControladorFuncionesCompartidas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shared.dto.user.CambioAvatarDTO;
import shared.dto.user.CambioContraDTO;
import shared.dto.user.CambioCorreoDTO;
import shared.dto.user.CambioNombreDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorAjustes extends ControladorFuncionesCompartidas {

    @FXML private StackPane rootAjustes;

    @FXML private Button btnCambiarNombre;
    @FXML private Button btnCambiarCorreo;
    @FXML private Button btnCambiarContra;
    @FXML private Button btnSeleccionarImg;
    @FXML private Button btnCambiarAvatar;
    @FXML private Button btnCancelarAjustes;

    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoCorreo;
    @FXML private TextField txtNuevaContra;

    private byte[] avatarBytesSeleccionado;

    @FXML
    private void initialize() {
        inicializarBotones();
    }

    private void inicializarBotones() {

        // CAMBIO NOMBRE
        btnCambiarNombre.setOnAction(e -> {
            String nuevo = safeTrim(txtNuevoNombre);
            if (nuevo.isBlank()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Cambiar Nombre", "El nombre no puede estar vacío.");
                return;
            }

            UserDTO u = Sesion.getUsuario();
            if (u == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay sesión iniciada.");
                return;
            }

            peticionCambioDatos(
                    "CAMBIO_NOMBRE",
                    new CambioNombreDTO(u.nombreUsuario, nuevo),
                    "Cambio de Nombre",
                    "Error al Cambiar Nombre"
            );
        });

        // CAMBIO CORREO
        btnCambiarCorreo.setOnAction(e -> {
            String nuevo = safeTrim(txtNuevoCorreo);
            if (nuevo.isBlank()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Cambiar Correo", "El correo no puede estar vacío.");
                return;
            }

            UserDTO u = Sesion.getUsuario();
            if (u == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay sesión iniciada.");
                return;
            }

            peticionCambioDatos(
                    "CAMBIO_CORREO",
                    new CambioCorreoDTO(u.nombreUsuario, nuevo),
                    "Cambio de Correo",
                    "Error al Cambiar Correo"
            );
        });

        // CAMBIO CONTRASEÑA
        btnCambiarContra.setOnAction(e -> {
            String nueva = safeTrim(txtNuevaContra);
            if (nueva.isBlank()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Cambiar Contraseña", "La contraseña no puede estar vacía.");
                return;
            }

            UserDTO u = Sesion.getUsuario();
            if (u == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay sesión iniciada.");
                return;
            }

            peticionCambioDatos(
                    "CAMBIO_CONTRA",
                    new CambioContraDTO(u.nombreUsuario, nueva),
                    "Cambio de Contraseña",
                    "Error al Cambiar Contraseña"
            );
        });

        // AVATAR
        btnSeleccionarImg.setOnAction(e -> onElegirAvatar());
        btnCambiarAvatar.setOnAction(e -> onCambiarAvatar());

        // CANCELAR
        btnCancelarAjustes.setOnAction(this::onCancelar);
    }

    // ====== SELECCIONAR AVATAR ======
    @FXML
    private void onElegirAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(rootAjustes.getScene().getWindow());
        if (archivo == null) return;

        try {
            avatarBytesSeleccionado = Files.readAllBytes(archivo.toPath());
            mostrarAlerta(Alert.AlertType.INFORMATION, "Avatar", "Imagen seleccionada correctamente.");
        } catch (IOException e) {
            avatarBytesSeleccionado = null;
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Avatar", "No se pudo leer la imagen: " + e.getMessage());
        }
    }

    // ====== CAMBIAR AVATAR ======
    @FXML
    private void onCambiarAvatar() {

        UserDTO u = Sesion.getUsuario();
        if (u == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay sesión iniciada.");
            return;
        }

        if (avatarBytesSeleccionado == null || avatarBytesSeleccionado.length == 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "Avatar", "Primero selecciona una imagen.");
            return;
        }

        // Envía al servidor. El servidor debe actualizar y, si puede, devolver el UserDTO actualizado en resp.data
        peticionCambioDatos(
                "CAMBIO_AVATAR",
                new CambioAvatarDTO(u.nombreUsuario, avatarBytesSeleccionado),
                "Cambio de Avatar",
                "Error al Cambiar Avatar"
        );
    }

    // ====== PETICIÓN GENÉRICA ======
    private void peticionCambioDatos(String accion, Serializable payload, String tituloOk, String tituloError) {

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion(accion, payload));

                Platform.runLater(() -> {
                    if (resp.ok) {

                        // 1) Actualiza sesión (ideal si servidor devuelve user)
                        if (resp.data instanceof UserDTO actualizado) {
                            Sesion.setUsuario(actualizado);
                        } else {
                            // 2) Fallback: actualiza localmente (para clase)
                            UserDTO u = Sesion.getUsuario();
                            if (u != null) {
                                switch (accion) {
                                    case "CAMBIO_NOMBRE" -> u.nombreUsuario = safeTrim(txtNuevoNombre);
                                    case "CAMBIO_CORREO" -> u.correo = safeTrim(txtNuevoCorreo);
                                    case "CAMBIO_CONTRA" -> u.contra = safeTrim(txtNuevaContra);
                                    case "CAMBIO_AVATAR" -> u.avatarImg = avatarBytesSeleccionado;
                                }
                                Sesion.setUsuario(u);
                            }
                        }

                        // ✅ Limpia el campo correspondiente SOLO si ok
                        limpiarCamposTrasExito(accion);

                        mostrarAlerta(Alert.AlertType.INFORMATION, tituloOk, resp.message);

                    } else {
                        mostrarAlerta(Alert.AlertType.ERROR, tituloError, resp.message);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage())
                );
            }
        }).start();
    }

    private void limpiarCamposTrasExito(String accion) {
        switch (accion) {
            case "CAMBIO_NOMBRE" -> txtNuevoNombre.clear();
            case "CAMBIO_CORREO" -> txtNuevoCorreo.clear();
            case "CAMBIO_CONTRA" -> txtNuevaContra.clear();
            case "CAMBIO_AVATAR" -> avatarBytesSeleccionado = null; // limpia selección
        }
    }

    private String safeTrim(TextField tf) {
        if (tf == null || tf.getText() == null) return "";
        return tf.getText().trim();
    }

    // ====== CERRAR VENTANA ======
    @FXML
    private void onCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
