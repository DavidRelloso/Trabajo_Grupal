package controller.main.friends.components;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import shared.dto.friends.ResponderSolicitudDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorTarjetaPeticion extends ControladorFuncionesCompartidas {

    @FXML private Label nombreAmigo;
    @FXML private Button btnRechazar;
    @FXML private Button btnAceptar;
    @FXML private HBox rootTarjetaPeticion;

    private String nombreSolicitud; 

    @FXML
    private void initialize() {

        btnAceptar.setOnAction(e -> aceptar());
        btnRechazar.setOnAction(e -> rechazar());
    }

    public void setDatos(String nombreSolicitud) {
        this.nombreSolicitud = nombreSolicitud;
        nombreAmigo.setText("Solicitud de " + nombreSolicitud);
    }

    private void aceptar() {
        responder("ACEPTAR_SOLICITUD_AMISTAD");
    }

    private void rechazar() {
        responder("RECHAZAR_SOLICITUD_AMISTAD");
    }

    private void responder(String accion) {
        try {
            ResponderSolicitudDTO dto = new ResponderSolicitudDTO(
                nombreSolicitud,
                Sesion.getUsuario().nombreUsuario
            );

            Respuesta r = Sesion.tcp.enviar(new Peticion(accion, dto));

            if (r.ok) {
                rootTarjetaPeticion.getChildren().clear();
                rootTarjetaPeticion.setManaged(false);
                rootTarjetaPeticion.setVisible(false);
            } else {
                mostrarAlerta(AlertType.ERROR, "Error", r.message);
            }

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", e.getMessage());
        }
    }
}
