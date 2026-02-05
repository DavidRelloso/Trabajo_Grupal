package controller.main;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.diary.ControladorDiarioPersonal;
import controller.sceneNavigator.NavegadorVentanas;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.dto.notes.DiaConNotasDTO;
import shared.dto.notes.NotaDiarioDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorPantallaPrincipal extends ControladorFuncionesCompartidas {

	@FXML private StackPane rootPrincipal;

	@FXML private Label lblNombreUsuario;
	@FXML private Circle imgAvatarUsuario;
	@FXML private DatePicker datePickerRecordatorios;

	@FXML private Accordion notificacionesAccordion;
	@FXML private Accordion recordatoriosAccordion;

	@FXML private Button btnVerDiario;
	@FXML private Button btnVerAmigos;
	@FXML private Button btnVerPeticiones;
	@FXML private Button btnAgregar;
	@FXML private Button btnVerAjustes;
	@FXML private Button btnGenerarInforme;
	@FXML private Button btnCerrarSesion;

	private LocalDate fechaSeleccionada = LocalDate.now();

	@FXML
	private void initialize() {

		btnVerDiario.setOnAction(e -> verDiario());
		btnVerAmigos.setOnAction(e -> verAmigos());
		btnAgregar.setOnAction(e -> agregarAmigo());
		btnVerPeticiones.setOnAction(e -> verPeticiones());
		btnGenerarInforme.setOnAction(e -> generarInformeJasper());
		btnVerAjustes.setOnAction(e -> verAjustes());
		btnCerrarSesion.setOnAction(e -> onCerrarSesion());

		if (datePickerRecordatorios != null) {
			datePickerRecordatorios.setValue(fechaSeleccionada);
			datePickerRecordatorios.valueProperty().addListener((obs, oldV, newV) -> {
				if (newV != null) {
					fechaSeleccionada = newV;
					cargarRecordatorios(fechaSeleccionada);
				}
			});
		}

		Sesion.usuarioProperty().addListener((obs, oldU, newU) -> refrescarHeader(newU));
		refrescarHeader(Sesion.getUsuario());

		cargarRecordatorios(fechaSeleccionada);
	}

	//CAMBIO DE DATOS EN PANEL USUARIO
	private void refrescarHeader(UserDTO u) {
		if (lblNombreUsuario != null) {
			lblNombreUsuario.setText(u != null ? u.nombreUsuario : "");
		}
		if (imgAvatarUsuario != null) {
			if (u != null && u.avatarImg != null) {
				Image image = new Image(new ByteArrayInputStream(u.avatarImg));
				imgAvatarUsuario.setFill(new ImagePattern(image));
			} else {
				imgAvatarUsuario.setFill(null);
			}
		}
	}

	

	//  CARGAR RECORDATORIOS 
	private void cargarRecordatorios(LocalDate fecha) {
		if (recordatoriosAccordion == null || fecha == null)
			return;

		recordatoriosAccordion.getPanes().clear();

		new Thread(() -> {
			try {
				Respuesta resp = enviar(new Peticion("CARGAR_DIARIO", null));

				Platform.runLater(() -> {
					if (resp == null || !resp.ok) {
						String msg = (resp == null) ? "Respuesta nula del servidor." : resp.message;
						mostrarAlerta(Alert.AlertType.ERROR, "Recordatorios", msg);
						return;
					}

					if (!(resp.data instanceof List<?> lista)) {
						mostrarAlerta(Alert.AlertType.ERROR, "Recordatorios", "Respuesta inválida (no es lista).");
						return;
					}

					@SuppressWarnings("unchecked")
					List<DiaConNotasDTO> dias = (List<DiaConNotasDTO>) lista;

					recordatoriosAccordion.getPanes().clear();

					for (DiaConNotasDTO d : dias) {
						if (d == null || d.fecha == null)
							continue;
						if (!fecha.equals(d.fecha))
							continue;
						if (d.notas == null || d.notas.isEmpty())
							continue;

						for (NotaDiarioDTO n : d.notas) {
							if (n == null)
								continue;

							try {
								FXMLLoader loader = new FXMLLoader(getClass().getResource(
										"/componentesReusables/principal/ComponenteRecordatoriosDiarios.fxml"));

								TitledPane tp = loader.load();
								ControladorRecordatorios c = loader.getController();

								LocalTime hora = n.horaInicio; 
								c.setInfoRecordatorio(n.titulo, 
										d.fecha, 
										hora, 
										n.texto 
								);

								c.setOnEliminar(() -> recordatoriosAccordion.getPanes().remove(tp));
								recordatoriosAccordion.getPanes().add(tp);

							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
				});

			} catch (Exception e) {
				Platform.runLater(
						() -> mostrarAlerta(Alert.AlertType.ERROR, "Recordatorios", "No conecta: " + e.getMessage()));
			}
		}, "cargar-recordatorios").start();
	}

	// ---------------- NAVEGACIÓN / OTROS ----------------
	private void verDiario() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/diario/VentanaDiarioPersonal.fxml"));
			Parent root = loader.load();

			ControladorDiarioPersonal ctrl = loader.getController();
			ctrl.setDiarioPropio();
			ctrl.cargar();

			Stage stage = (Stage) btnVerDiario.getScene().getWindow();
			stage.getScene().setRoot(root);

		} catch (Exception ex) {
			ex.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la pantalla diario: " + ex.getMessage());
		}
	}

	private void verAmigos() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaListaAmigos.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initOwner(rootPrincipal.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.sizeToScene();
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Amigos: " + e.getMessage());
		}
	}

	private void agregarAmigo() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaAgregarAmigo.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initOwner(rootPrincipal.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.sizeToScene();
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Amigos: " + e.getMessage());
		}
	}

	private void verPeticiones() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaPeticionAmigo.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initOwner(rootPrincipal.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.sizeToScene();
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Amigos: " + e.getMessage());
		}
	}

	private void generarInformeJasper() {
		Task<Void> tarea = new Task<>() {
			@Override
			protected Void call() throws Exception {
				Sesion.asegurarConexion();
				byte[] pdf = Sesion.tcp.solicitarInformeUsuarios();
				try (FileOutputStream fos = new FileOutputStream("informe_usuarios.pdf")) {
					fos.write(pdf);
				}
				return null;
			}
		};

		tarea.setOnSucceeded(ev -> abrirPDF("informe_usuarios.pdf"));
		tarea.setOnFailed(ev -> {
			Throwable ex = tarea.getException();
			if (ex != null)
				ex.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Informe", ex != null ? ex.getMessage() : "Error desconocido");
		});

		new Thread(tarea, "informe-task").start();
	}

	private void abrirPDF(String ruta) {
		try {
			java.awt.Desktop.getDesktop().open(new java.io.File(ruta));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void verAjustes() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaAjustes.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.initOwner(rootPrincipal.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.sizeToScene();
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Ajustes: " + e.getMessage());
		}
	}

	private void onCerrarSesion() {
		try {
			Sesion.cerrarSesion();
			NavegadorVentanas.navegar("/escenas/login/VentanaLogin.fxml");
		} catch (Exception ex) {
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Login: " + ex.getMessage());
		}
	}
}
