package controller.main.friends;

import java.util.Collections;
import java.util.List;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.main.friends.components.ControladorTarjetaAmigos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import shared.dto.friends.AmigoDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorListaAmigos extends ControladorFuncionesCompartidas {

	
	@FXML private StackPane rootListaAmigos;
    @FXML private VBox contenedorAmigos;

    @FXML
    private void initialize() {
        recuperarAmigosAsync();
    }

    // Recuperar amigos
    private void recuperarAmigosAsync() {
        new Thread(() -> {
            try {
                Respuesta resp = Sesion.tcp.enviar(new Peticion("OBTENER_AMIGOS", null));

                if (resp == null) {
                    Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "Respuesta nula del servidor.")
                    );
                    return;
                }

                if (!resp.ok) {
                    String msg = resp.message != null ? resp.message : "Error desconocido";
                    Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar amigos: " + msg)
                    );
                    return;
                }

                @SuppressWarnings("unchecked")
                List<AmigoDTO> amigos = (resp.data instanceof List)
                        ? (List<AmigoDTO>) resp.data
                        : Collections.emptyList();

                Platform.runLater(() -> pintarAmigos(amigos));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los amigos: " + e.getMessage())
                );
            }
}, "cargar-amigos").start();
    }

    private void pintarAmigos(List<AmigoDTO> amigos) {
        contenedorAmigos.getChildren().clear();

        if (amigos == null || amigos.isEmpty()) {
            return;
        }

        for (AmigoDTO amigo : amigos) {
            crearTarjetaAmigo(amigo);
        }
    }

    private void crearTarjetaAmigo(AmigoDTO dto) {
        if (dto == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/componentesReusables/amigos/ComponenteTarjetaAmigo.fxml")
            );

            Node tarjetaAmigo = loader.load();

            ControladorTarjetaAmigos c = loader.getController();
            // Ajusta esto a tu DTO real (dto.nombreUsuario o dto.getNombreUsuario())
            c.setDatos(dto.nombreUsuario, rootListaAmigos);

            contenedorAmigos.getChildren().add(tarjetaAmigo);

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudo crear la tarjeta de amigo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
