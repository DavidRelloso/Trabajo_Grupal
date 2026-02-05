package controller.diary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.ControladorFuncionesCompartidas;
import controller.diary.components.ControladorColumnaDia;
import controller.diary.components.ControladorNota;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.dto.notes.CrearDiaDTO;
import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.DiaConNotasDTO;
import shared.dto.notes.EliminarDiaDTO;
import shared.dto.notes.EliminarNotaDTO;
import shared.dto.notes.NotaDiarioDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorDiarioPersonal extends ControladorFuncionesCompartidas{

	@FXML private StackPane rootDiario;
	@FXML private HBox contenedorColumnas;
	
	@FXML private ComboBox<String> cbCategoria;
	@FXML private ComboBox<String> cbOrden;
	
	@FXML private Button btnLogoImg;
	@FXML private Button btnAgregarDia;
	@FXML private Button openOffcanvas;
	
	private final Map<Long, VBox> contenedoresNotasPorDia = new HashMap<>();
	
    private boolean diarioPropio = true;
    private String usuarioDiario = null;
	
	@FXML
	private void initialize() {

		cbCategoria.getItems().setAll("TODAS LAS CATEGORIAS","OCIO", "TRABAJO", "ESTUDIO");
		cbCategoria.getSelectionModel().selectFirst(); 
		cbCategoria.valueProperty().addListener((obs, oldVal, newVal) -> {
		    filtrarPorCategoria(newVal);
		});

		cbOrden.getItems().setAll("CERCANAS PRIMERO", "ÚLTIMAS PRIMERO");
		cbOrden.valueProperty().addListener((obs, oldVal, newVal) -> {
			ordenarColumnasPorFecha(newVal);
		});
		
		btnLogoImg.setOnAction(e -> volverInicio());
		btnAgregarDia.setOnAction(e -> pantallaCrearNota());
		
	}
	
	
    public void setDiarioPropio() {
        this.diarioPropio = true;
        this.usuarioDiario = null;
    }

    public void setDiarioAmigo(String nombreAmigo) {
        this.diarioPropio = false;
        this.usuarioDiario = (nombreAmigo != null) ? nombreAmigo.trim() : null;
    }

    // ---------- ENTRADA ÚNICA DE CARGA ----------
    public void cargar() {
        if (diarioPropio) {
            btnAgregarDia.setVisible(true);
            btnAgregarDia.setManaged(true);
            cargarDiarioPropio();          // no pasa nombre
        } else {
            btnAgregarDia.setVisible(false);
            btnAgregarDia.setManaged(false);
            cargarDiarioAmigo(usuarioDiario); // sí pasa nombre amigo
        }
    }

	
	// CARGAR NOTAS AL ENTRAR AL DIARIO
    private void cargarDiarioPropio() {
        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion("CARGAR_DIARIO", null));
                Platform.runLater(() -> pintarDiario(resp));
            } catch (Exception e) {
                Platform.runLater(() ->
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage())
                );
            }
        }).start();
    }

    private void cargarDiarioAmigo(String nombreAmigo) {
        if (nombreAmigo == null || nombreAmigo.isBlank()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Nombre de amigo inválido.");
            return;
        }

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion("CARGAR_DIARIO_AMIGO", nombreAmigo));
                Platform.runLater(() -> pintarDiario(resp));
            } catch (Exception e) {
                Platform.runLater(() ->
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage())
                );
            }
        }).start();
    }
	
    
    private void pintarDiario(Respuesta resp) {
        if (!resp.ok) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", resp.message);
            return;
        }

        if (!(resp.data instanceof List<?> lista)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Respuesta inválida (no es lista).");
            return;
        }

        contenedorColumnas.getChildren().clear();
        contenedoresNotasPorDia.clear();

        @SuppressWarnings("unchecked")
        List<DiaConNotasDTO> dias = (List<DiaConNotasDTO>) lista;

        for (DiaConNotasDTO d : dias) {
            CrearDiaDTO diaUi = new CrearDiaDTO(true, d.idDia, null, d.fecha, d.categoria);
            crearColumnaDia(diaUi);

            if (d.notas != null) {
                for (NotaDiarioDTO n : d.notas) {
                    CrearNotaDTO fake = new CrearNotaDTO(
                        d.fecha,
                        d.categoria,
                        n.titulo,
                        n.texto,
                        n.horaInicio,
                        n.horaFin,
                        n.visibilidad
                    );
                    insertarBloqueNota(d.idDia, n.idNota, fake);
                }
            }
        }
    }

    
	// ABRIR PANTALLA CREAR NOTA
	private void pantallaCrearNota() {
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/diario/VentanaCrearNota.fxml"));
            Parent root = loader.load();

            ControladorCrearNota ctrl = loader.getController();
            ctrl.setListener((out, dto) -> {

                if (out.diaCreado) {
                    crearColumnaDia(out);
                }

                if (!contenedoresNotasPorDia.containsKey(out.idDia)) {
                    crearColumnaDia(out);
                }

                insertarBloqueNota(out.idDia, out.idNota, dto);
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
	            getClass().getResource("/componentesReusables/notas/ComponenteNotaDiario.fxml")
	        );
	        Node card = loader.load();

	       
	        ControladorNota c = loader.getController();
	        c.setPadre(this);
	        c.setDatosNota(idDia, idNota, dto);

	        VBox contenedor = contenedoresNotasPorDia.get(idDia);
	        if (contenedor == null) {
	            mostrarAlerta(Alert.AlertType.ERROR, "Diario", "No existe contenedor para el día " + idDia);
	            return;
	        }
	        
	        System.out.println("Map keys: " + contenedoresNotasPorDia.keySet());
	        System.out.println("Busco idDia=" + idDia);

	        card.getProperties().put("horaInicio", dto.horaInicio);
	        contenedor.getChildren().add(card);
	        ordenarNotaPorHora(contenedor);
	        
	    } catch (Exception e) {
	        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo crear bloque nota: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	// CREAR COLUMNA DIA
	private void crearColumnaDia(CrearDiaDTO dto) {

	    if (contenedoresNotasPorDia.containsKey(dto.idDia)) return;

	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/componentesReusables/notas/ComponenteColumnaDia.fxml")
	        );
	        Node columna = loader.load();
	        
	        columna.getProperties().put("fecha", dto.fecha);
	        columna.getProperties().put("categoria", dto.categoria);

	        ControladorColumnaDia c = loader.getController();
	        c.setPadre(this);
	        c.setDatosDia(dto.idDia, dto.fecha, dto.categoria);

	        contenedoresNotasPorDia.put(dto.idDia, c.getContenedorNotas());
	        contenedorColumnas.getChildren().add(columna);
	        
	        ordenarColumnasPorFecha("CERCANAS PRIMERO");

	    } catch (Exception e) {
	        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo crear columna día: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	// ORDENAR NOTAS POR HORA INICIO
	private void ordenarNotaPorHora(VBox contenedor) {
	    var ordenadas = contenedor.getChildren().stream()
	        .sorted((a, b) -> {
	            LocalTime ha = (LocalTime) a.getProperties().get("horaInicio");
	            LocalTime hb = (LocalTime) b.getProperties().get("horaInicio");

	            if (ha == null && hb == null) return 0;
	            if (ha == null) return 1;
	            if (hb == null) return -1;

	            return ha.compareTo(hb);
	        })
	        .toList();

	    contenedor.getChildren().setAll(ordenadas);
	}

	
	// VUELVE PANTALLA INICIO
	private void volverInicio() {
	System.out.println("volviendo a nicio");
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaPantallaPrincipal.fxml"));
		Parent root = loader.load();

		Stage stage = (Stage) rootDiario.getScene().getWindow();
		stage.getScene().setRoot(root);
	} catch (Exception ex) {
		    mostrarAlerta(Alert.AlertType.ERROR, "Error",
		        "No se pudo abrir la pantalla principal: " + ex.getMessage());
		}
	}
	
	// FILTRAR NOTAS POR CATEGORIA
	private void filtrarPorCategoria(String categoriaSeleccionada) {

	    for (Node columna : contenedorColumnas.getChildren()) {

	        String categoriaColumna = (String) columna.getProperties().get("categoria");

	        boolean visible = categoriaSeleccionada == null
	            || categoriaSeleccionada.equalsIgnoreCase("TODAS LAS CATEGORIAS")
	            || categoriaSeleccionada.equalsIgnoreCase(categoriaColumna);

	        columna.setVisible(visible);
	        columna.setManaged(visible); 
	    }
	}
	
	// ORDENAR COLUMNAS POR FECHA
	private void ordenarColumnasPorFecha(String ordenSeleccionado) {
	    var ordenadas = contenedorColumnas.getChildren().stream()
	        .sorted((a, b) -> {
	            LocalDate fa = (LocalDate) a.getProperties().get("fecha");
	            LocalDate fb = (LocalDate) b.getProperties().get("fecha");

	            if (fa == null && fb == null) return 0;
	            if (fa == null) return 1;
	            if (fb == null) return -1;

	            return ordenSeleccionado.equals("CERCANAS PRIMERO") 
	            		? fa.compareTo(fb)  //CERCANAS PRIMERO 
	            		: fb.compareTo(fa); //ULTIMAS PRMERO
	        })
	        .toList();

	    contenedorColumnas.getChildren().setAll(ordenadas);
	}
	

public void onAgregarNotaEnDia(Long idDia, LocalDate fecha, String categoria) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/diario/VentanaCrearNota.fxml"));
        Parent root = loader.load();

        ControladorCrearNota ctrl = loader.getController();
        try { ctrl.prefijarFechaYCategoria(fecha, categoria); } catch (Exception ignored) {}

        ctrl.setListener((out, dto) -> {
            if (out.diaCreado) crearColumnaDia(out);
            if (!contenedoresNotasPorDia.containsKey(out.idDia)) crearColumnaDia(out);
            insertarBloqueNota(out.idDia, out.idNota, dto);
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
        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir 'Crear Nota': " + ex.getMessage());
    }
}

public void onEliminarNota(Long idDia, Long idNota, Node cardRoot) {
    if (idDia == null || idNota == null) return;

    var confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar esta nota?", ButtonType.OK, ButtonType.CANCEL);
    confirm.setHeaderText(null);
    confirm.showAndWait();
    if (confirm.getResult() != ButtonType.OK) return;

    new Thread(() -> {
        try {
            Respuesta resp = enviar(new Peticion("ELIMINAR_NOTA",
                    new EliminarNotaDTO(idDia, idNota)));

            Platform.runLater(() -> {
                if (!resp.ok) { mostrarAlerta(Alert.AlertType.ERROR, "Eliminar nota", resp.message); return; }
                VBox cont = contenedoresNotasPorDia.get(idDia);
                if (cont != null) cont.getChildren().remove(cardRoot);
            });

        } catch (Exception e) {
            Platform.runLater(() ->
                mostrarAlerta(Alert.AlertType.ERROR, "Eliminar nota", "No conecta: " + e.getMessage())
            );
        }
    }).start();
}

public void onEliminarDia(Long idDia, Node columnaRoot) {
    if (idDia == null) return;

    var confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el día completo y sus notas?", ButtonType.OK, ButtonType.CANCEL);
    confirm.setHeaderText(null);
    confirm.showAndWait();
    if (confirm.getResult() != ButtonType.OK) return;

    new Thread(() -> {
        try {
            Respuesta resp = enviar(new Peticion("ELIMINAR_DIA",
                    new EliminarDiaDTO(idDia)));

            Platform.runLater(() -> {
                if (!resp.ok) { mostrarAlerta(Alert.AlertType.ERROR, "Eliminar día", resp.message); return; }
                contenedorColumnas.getChildren().remove(columnaRoot);
                contenedoresNotasPorDia.remove(idDia);
            });

        } catch (Exception e) {
            Platform.runLater(() ->
                mostrarAlerta(Alert.AlertType.ERROR, "Eliminar día", "No conecta: " + e.getMessage())
            );
        }
    }).start();
}


	
}
