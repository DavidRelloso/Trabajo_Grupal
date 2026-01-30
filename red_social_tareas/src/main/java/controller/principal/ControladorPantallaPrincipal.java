package controller.principal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import client.Sesion;
import controller.ControladorFuncionesCompartidas;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.dto.NotificacionDTO;
import shared.dto.user.UserDTO;

public class ControladorPantallaPrincipal extends ControladorFuncionesCompartidas {

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

    private LocalDate fechaSeleccionada = LocalDate.now();

    @FXML
    private void initialize() {

        // Botones
        btnVerDiario.setOnAction(e -> onVerDiario());
        btnVerAmigos.setOnAction(e -> onVerAmigos());
        btnVerAjustes.setOnAction(e -> onVerAjustes());
        btnCerrarSesion.setOnAction(e -> onCerrarSesion());

        // DatePicker
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

        cargarNotificaciones();
        cargarRecordatorios(fechaSeleccionada);
    }

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

    // ====== NOTIFICACIONES ======
    private void cargarNotificaciones() {
        notificacionesAccordion.getPanes().clear();

        List<NotificacionDTO> notifs = new ArrayList<>();
        notifs.add(new NotificacionDTO("Solicitud de amistad", "Pepe te ha enviado una solicitud."));
        notifs.add(new NotificacionDTO("Nuevo mensaje", "Tienes un mensaje nuevo de Ana."));

        for (NotificacionDTO n : notifs) {
            notificacionesAccordion.getPanes().add(crearTitledPaneNotificacion(n));
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

    // ====== RECORDATORIOS (mock) ======
    private void cargarRecordatorios(LocalDate fecha) {
        recordatoriosAccordion.getPanes().clear();

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
        btnEliminar.setOnAction(e -> recordatoriosAccordion.getPanes().removeIf(p -> p.getText().equals(r.fecha.toString())));

        StackPane right = new StackPane(btnEliminar);
        StackPane.setAlignment(btnEliminar, javafx.geometry.Pos.CENTER_RIGHT);

        VBox box = new VBox(10, texto, sep, right);
        box.setPadding(new Insets(10));

        TitledPane tp = new TitledPane(r.fecha.toString(), box);
        tp.setAnimated(false);
        return tp;
    }

    // ====== BOTONES PANEL IZQUIERDO ======
    private void onVerDiario() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "MI DIARIO", "Aquí abrirías la pantalla de Diario.");
    }

    private void onVerAmigos() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "AMIGOS", "Aquí abrirías la pantalla de Amigos.");
    }

    private void onVerAjustes() {
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/login/VentanaLogin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPrincipal.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir Login: " + ex.getMessage());
        }
    }

    // ====== DTO interno para mock ======
    public static class RecordatorioDTO {
        public LocalDate fecha;
        public String texto;

        public RecordatorioDTO(LocalDate fecha, String texto) {
            this.fecha = fecha;
            this.texto = texto;
        }
    }
}
