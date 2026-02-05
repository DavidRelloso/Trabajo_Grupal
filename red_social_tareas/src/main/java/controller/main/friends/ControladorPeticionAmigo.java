package controller.main.friends;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.main.friends.components.ControladorTarjetaPeticion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shared.dto.friends.SolicitudAmistadDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorPeticionAmigo extends ControladorFuncionesCompartidas{

	@FXML private VBox seccionPeticiones;
	@FXML private Button btnActualizar;

	private final Set<String> yaMostradas = ConcurrentHashMap.newKeySet();

	private final Consumer<SolicitudAmistadDTO> listener = dto ->{
	    System.out.println("PUSH recibido: " + dto.getFromUsername());
		Platform.runLater(() -> crearTarjetaSolicitud(dto));
	};

	@FXML
	private void initialize() {
	    Sesion.tcp.addSolicitudListener(listener);     
	    btnActualizar.setOnAction(e -> {
	        System.out.println("Actualizar peticiones");
	        recuperarSolicitudes();
	    });
	    recuperarSolicitudes();
	} 
	
	// CARGAR SOLICITUDES PENDIENTES
	private void recuperarSolicitudes() {

	    Platform.runLater(() -> {
	        seccionPeticiones.getChildren().clear();
	        yaMostradas.clear();
	    });

	    new Thread(() -> {
	        try {
	            Respuesta resp = Sesion.tcp.enviar(
	                new Peticion("OBTENER_SOLICITUDES_PENDIENTES", null)
	            );

	            System.out.println("ACTUALIZAR resp.ok=" + resp.ok + " msg=" + resp.message);

	            if (resp.ok && resp.data instanceof List<?> list) {
	                System.out.println("ACTUALIZAR size=" + list.size());

	                for (Object o : list) {
	                    if (o instanceof SolicitudAmistadDTO dto) {
	                        Platform.runLater(() -> crearTarjetaSolicitud(dto));
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }, "cargar-peticiones").start();
	}


	//CREAR NUEVA TARJETA SOLICITUD
	private void crearTarjetaSolicitud(SolicitudAmistadDTO dto) {
	    String key = dto.getFromUsername() + "->" + dto.getToUsername(); 
	    if (!yaMostradas.add(key)) return;

	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/componentesReusables/amigos/ComponenteTarjetaPeticion.fxml")
	        );
	        Node tarjetaPeticion = loader.load();
	        seccionPeticiones.getChildren().add(0, tarjetaPeticion);

	        ControladorTarjetaPeticion c = loader.getController();
	        c.setDatos(dto.getFromUsername());

	    } catch (Exception e) {
	        mostrarAlerta(Alert.AlertType.ERROR, "Error",
	            "No se pudo crear tarjeta de solicitud: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


}
