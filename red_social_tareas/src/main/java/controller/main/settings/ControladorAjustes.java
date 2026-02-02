package controller.main.settings;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.function.BiFunction;

import client.net.Sesion;
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
			configurarCambio(
					btnCambiarNombre, 
					txtNuevoNombre, 
					"El nombre no puede estar vacío.", 
					"CAMBIO_NOMBRE",
					CambioNombreDTO::new, 
					"Cambio de Nombre", 
					"Error al Cambiar Nombre"
			);
		});
    	
        // CAMBIO CORREO
		btnCambiarCorreo.setOnAction(e -> {
			configurarCambio(
			        btnCambiarCorreo,
			        txtNuevoCorreo,
			        "El correo no puede estar vacío.",
			        "CAMBIO_CORREO",
			        CambioCorreoDTO::new,
			        "Cambio de Correo",
			        "Error al Cambiar Correo"
			);
		});

        // CAMBIO CONTRASEÑA
		btnCambiarContra.setOnAction(e -> {
			configurarCambio(
			        btnCambiarContra,
			        txtNuevaContra,
			        "La contraseña no puede estar vacía.",
			        "CAMBIO_CONTRA",
			        CambioContraDTO::new,
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
    
    //funcion para reusar las peticiones de cambio de los botones
    private <T extends Serializable> void configurarCambio(
            Button boton,
            TextField input,
            String errorVacio,
            String tipoOperacion,
            BiFunction<String, String, T> dtoFactory,
            String tituloExito,
            String tituloError
    ) {
        boton.setOnAction(e -> {
            String nuevo = safeTrim(input);
            if (nuevo.isBlank()) {
                mostrarAlerta(Alert.AlertType.ERROR, tituloError, errorVacio);
                return;
            }

            UserDTO u = Sesion.getUsuario();
            if (u == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay sesión iniciada.");
                return;
            }

            peticionCambioDatos(
                    tipoOperacion,
                    dtoFactory.apply(u.nombreUsuario, nuevo), 
                    tituloExito,
                    tituloError
            );
        });
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

    // ====== CAMBIAR AVATAR =====
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

        peticionCambioDatos(
                "CAMBIO_AVATAR",
                new CambioAvatarDTO(u.nombreUsuario, avatarBytesSeleccionado),
                "Cambio de Avatar",
                "Error al Cambiar Avatar"
        );
    }

    // ==== PETICIÓN GENÉRICA ======
    private void peticionCambioDatos(String accion, Serializable payload, String tituloOk, String tituloError) {

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion(accion, payload));

                Platform.runLater(() -> {
                    if (resp.ok) {

                        if (resp.data instanceof UserDTO actualizado) {
                            Sesion.setUsuario(actualizado);
                        } else {
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
            case "CAMBIO_AVATAR" -> avatarBytesSeleccionado = null; 
        }
    }

    private String safeTrim(TextField tf) {
        if (tf == null || tf.getText() == null) return "";
        return tf.getText().trim();
    }

    // ===== CERRAR VENTANA ====
    @FXML
    private void onCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
