package controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import shared.NotificacionDTO;
import shared.UserDTO;

public class ControladorPantallaPrincipal {

	@FXML private Accordion notificacionesAccordion;
	@FXML private Accordion recordatoriosAccordion;

	@FXML private StackPane rootPrincipal;
	@FXML private Label lblNombreUsuario;
	@FXML private Circle imgAvatarUsuario;
	@FXML private DatePicker datePickerRecordatorios;

	@FXML private Button btnVerDiario;
	@FXML private Button btnVerAmigos;
	@FXML private Button btnVerAjustes;
	@FXML private Button btnCerrarSesion;

	// ====== ESTADO ======
	private UserDTO usuarioSesion; // usuario logueado (DTO/sesión)
	private LocalDate fechaSeleccionada = LocalDate.now();

	@FXML
	private void initialize() {
		// Acciones botones
		btnVerDiario.setOnAction(e -> onVerDiario());
		btnVerAmigos.setOnAction(e -> onVerAmigos());
		btnVerAjustes.setOnAction(e -> onVerAjustes());
		btnCerrarSesion.setOnAction(e -> onCerrarSesion());

		// DatePicker: cargar recordatorios por fecha
		if (datePickerRecordatorios != null) {
			datePickerRecordatorios.setValue(fechaSeleccionada);
			datePickerRecordatorios.valueProperty().addListener((obs, oldV, newV) -> {
				if (newV != null) {
					fechaSeleccionada = newV;
					cargarRecordatorios(fechaSeleccionada);
				}
			});
		}

		// Carga inicial “mock” (luego lo reemplazas por llamadas al servidor)
		cargarNotificaciones();
		cargarRecordatorios(fechaSeleccionada);
	}

	// ====== API PARA RECIBIR EL USUARIO DESDE LOGIN ======
	// Llama a esto desde el controlador de login al abrir esta pantalla
	public void setUsuarioSesion(UserDTO usuarioSesion) {
		this.usuarioSesion = usuarioSesion;

		if (lblNombreUsuario != null) {
			lblNombreUsuario.setText(usuarioSesion.nombreUsuario);
		}

		System.out.println(
				"avatar bytes: " + (usuarioSesion.avatarImg == null ? "null" : usuarioSesion.avatarImg.length));

		if (imgAvatarUsuario != null && usuarioSesion.avatarImg != null) {
			System.out.println("no null avatar");
			Image image = new Image(new ByteArrayInputStream(usuarioSesion.avatarImg));
			imgAvatarUsuario.setFill(new ImagePattern(image));
		}
	}

	// ====== CARGA DE NOTIFICACIONES ======
	private void cargarNotificaciones() {
		notificacionesAccordion.getPanes().clear();

		// Ejemplo (mock). Esto luego vendrá del servidor:
		List<NotificacionDTO> notifs = new ArrayList<>();
		notifs.add(new NotificacionDTO("Solicitud de amistad", "Pepe te ha enviado una solicitud."));
		notifs.add(new NotificacionDTO("Nuevo mensaje", "Tienes un mensaje nuevo de Ana."));

		for (NotificacionDTO n : notifs) {
			TitledPane tp = crearTitledPaneNotificacion(n);
			notificacionesAccordion.getPanes().add(tp);
		}
	}

	private TitledPane crearTitledPaneNotificacion(NotificacionDTO n) {

		Label texto = new Label(n.texto);
		texto.getStyleClass().add("etiqueta-campo");

		Separator sep = new Separator();

		Button btnEliminar = new Button("X");
		btnEliminar.getStyleClass().add("boton-eliminar");
		btnEliminar.setPrefWidth(30);
		btnEliminar.setPrefHeight(26);

		btnEliminar.setOnAction(e -> notificacionesAccordion.getPanes().removeIf(p -> p.getText().equals(n.titulo)));

		StackPane right = new StackPane(btnEliminar);
		StackPane.setMargin(btnEliminar, new Insets(0, 0, 0, 0));
		StackPane.setAlignment(btnEliminar, javafx.geometry.Pos.CENTER_RIGHT);

		VBox box = new VBox(10, texto, sep, right);
		box.setPadding(new Insets(10));

		TitledPane tp = new TitledPane(n.titulo, box);
		tp.setAnimated(false);
		return tp;
	}

	// ====== CARGA DE RECORDATORIOS POR FECHA ======
	private void cargarRecordatorios(LocalDate fecha) {
		recordatoriosAccordion.getPanes().clear();

		// Ejemplo (mock). Esto luego vendrá del servidor filtrado por fecha:
		List<RecordatorioDTO> recs = new ArrayList<>();
		recs.add(new RecordatorioDTO(fecha, "Beber agua"));
		recs.add(new RecordatorioDTO(fecha, "Entrenar 30 min"));

		for (RecordatorioDTO r : recs) {
			recordatoriosAccordion.getPanes().add(crearTitledPaneRecordatorio(r));
		}
	}

	private TitledPane crearTitledPaneRecordatorio(RecordatorioDTO r) {
		Label texto = new Label(r.texto);
		texto.getStyleClass().add("etiqueta-campo");

		Separator sep = new Separator();

		Button btnEliminar = new Button("X");
		btnEliminar.getStyleClass().add("boton-eliminar");
		btnEliminar.setPrefWidth(30);
		btnEliminar.setPrefHeight(26);

		btnEliminar.setOnAction(
				e -> recordatoriosAccordion.getPanes().removeIf(p -> p.getText().equals(r.fecha.toString())));

		StackPane right = new StackPane(btnEliminar);
		StackPane.setAlignment(btnEliminar, javafx.geometry.Pos.CENTER_RIGHT);

		VBox box = new VBox(10, texto, sep, right);
		box.setPadding(new Insets(10));

		TitledPane tp = new TitledPane(r.fecha.toString(), box);
		tp.setAnimated(false);
		return tp;
	}

	// ====== BOTONES ======
	private void onVerDiario() {
		mostrarAlerta(Alert.AlertType.INFORMATION, "MI DIARIO", "Aquí abrirías la pantalla de Diario.");
	}

	private void onVerAmigos() {
		mostrarAlerta(Alert.AlertType.INFORMATION, "AMIGOS", "Aquí abrirías la pantalla de Amigos.");
	}

	private void onVerAjustes() {
		mostrarAlerta(Alert.AlertType.INFORMATION, "AJUSTES", "Aquí abrirías la pantalla de Ajustes.");
	}

	private void onCerrarSesion() {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/VentanaLogin.fxml"));
			Parent root = loader.load();

			ControladorLogin controller = loader.getController();

			Stage stage = (Stage) rootPrincipal.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception ex) {
			mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la pantalla principal: " + ex.getMessage());
		}
	}

	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	// ====== DTOs internos (para que compile). En vuestro proyecto estarán en
	// "shared" ======
	public static class RecordatorioDTO {
		public LocalDate fecha;
		public String texto;

		public RecordatorioDTO(LocalDate fecha, String texto) {
			this.fecha = fecha;
			this.texto = texto;
		}
	}
}
