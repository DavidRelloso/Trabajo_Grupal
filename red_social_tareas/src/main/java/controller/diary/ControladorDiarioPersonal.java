package controller.diary;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import controller.ControladorFuncionesCompartidas;
import controller.sceneNavigator.NavegadorVentanas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.dto.notes.CrearNotaDTO;

public class ControladorDiarioPersonal extends ControladorFuncionesCompartidas{

	@FXML private StackPane rootDiario;
	@FXML private HBox contenedorColumnas;
	
	@FXML private Button btnLogoImg;
	@FXML private Button btnAgregarDia;
	@FXML private Button openOffcanvas;
	
	private final Map<Long, VBox> contenedoresNotasPorDia = new HashMap<>();
	
	@FXML
	private void initialize() {

		btnLogoImg.setOnAction(e -> volverInicio());
		btnAgregarDia.setOnAction(e -> pantallaCrearNota());
	}
	
	// ABRIR PANTALLA CREAR NOTA
	private void pantallaCrearNota() {
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/diario/VentanaCrearNota.fxml"));
            Parent root = loader.load();

            ControladorCrearNota ctrl = loader.getController();
            ctrl.setListener((out, dto) -> {

            	System.out.println(out.diaCreado);
            	if (out.diaCreado) {
                    crearColumnaDia(out.diaId, dto.fecha, dto.categoria);
                }
                insertarBloqueNota(out.diaId, out.notaId, dto);
            });
            
            
            Stage stage = new Stage();
            stage.initOwner(rootDiario.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Ajustes: " + ex.getMessage());
        }
	}
	
	// CREAR NOTA EN DIA
	private void insertarBloqueNota(Long idDia, Long idNota, CrearNotaDTO dto) {
	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/componentesReusables/ComponenteNotaDiario.fxml")
	        );
	        Node card = loader.load();

	        /*ControladorColumnaDia c = loader.getController();
	        c.setDatosDia(idNota, dto.fecha, dto.categoria);*/

	        VBox contenedor = contenedoresNotasPorDia.get(idDia);
	        if (contenedor == null) {
	            mostrarAlerta(Alert.AlertType.ERROR, "Diario", "No existe contenedor para el día " + idDia);
	            return;
	        }

	        contenedor.getChildren().add(card);

	    } catch (Exception e) {
	        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo crear bloque nota: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	// CREAR COLUMNA DIA
	private void crearColumnaDia(Long idDia, LocalDate fecha, String categoria) {
	    if (contenedoresNotasPorDia.containsKey(idDia)) return;

	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/componentesReusables/ComponenteColumnaDia.fxml")
	        );
	        Node columna = loader.load();

	        ControladorColumnaDia ctrl = loader.getController();
	        ctrl.setDatosDia(idDia, fecha, categoria);

	        contenedoresNotasPorDia.put(idDia, ctrl.getContenedorNotas());
	        contenedorColumnas.getChildren().add(columna);

	    } catch (Exception e) {
	        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo crear columna día: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	
	// VUELVE PANTALLA INICIO
	private void volverInicio() {
		
		try {
			NavegadorVentanas.navegar("/escenas/principal/VentanaPantallaPrincipal.fxml");
		} catch (Exception ex) {
		    mostrarAlerta(Alert.AlertType.ERROR, "Error",
		        "No se pudo abrir la pantalla principal: " + ex.getMessage());
		}

	}
	
	
	
}
