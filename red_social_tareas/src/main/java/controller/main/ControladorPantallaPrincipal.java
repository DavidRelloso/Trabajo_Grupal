package controller.main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import client.net.Sesion;
import controller.ControladorFuncionesCompartidas;
import controller.sceneNavigator.NavegadorVentanas;
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
    @FXML private Button btnCerrarSesion;

    private LocalDate fechaSeleccionada = LocalDate.now();

    @FXML
    private void initialize() {

        // Botones
        btnVerDiario.setOnAction(e -> verDiario());
        btnVerAmigos.setOnAction(e -> verAmigos());
        btnAgregar.setOnAction(e -> agregarAmigo());
        btnVerPeticiones.setOnAction(e -> verPeticiones());
        btnVerAjustes.setOnAction(e -> verAjustes());
        btnCerrarSesion.setOnAction(e -> onCerrarSesion());

		try {
			for (int i = 0; i < 10; i++) {

				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/componentesReusables/principal/ComponenteNotificacionAmigo.fxml"));
				Parent nuevoComponente = loader.load();

				TitledPane titledPane = (TitledPane) nuevoComponente;
				ControladorNotificaciones controller = loader.getController();
				// controller.setInfoNoti("NombreAmigo", "TEXTO_NOTIFICACION");
				controller.setInfoNoti("amigo",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent elementum urna ac feugiat convallis. In consectetur mollis dolor id mollis. Nunc ipsum eros, auctor id felis vitae, ultricies eleifend nibh. Aliquam erat volutpat. Donec hendrerit ex sapien. Curabitur ligula enim, mattis id lacus id, posuere suscipit ipsum. Mauris a odio eu ante condimentum ornare vel a mauris. Nulla facilisis velit quis justo faucibus vehicula. Maecenas felis velit, hendrerit non tempor a, cursus non risus.\r\n"
								+ "\r\n"
								+ "Donec venenatis, diam egestas gravida convallis, velit nulla blandit ligula, in suscipit ante sapien vitae est. Integer eget eleifend enim, eu euismod lorem. Sed luctus ante a mollis facilisis. Mauris facilisis nibh eget vehicula faucibus. Fusce sed erat eros. Ut eleifend euismod vulputate. Nullam feugiat libero a luctus elementum. Vivamus quis rutrum urna, feugiat malesuada magna. Etiam nec finibus orci, non eleifend odio. Donec scelerisque justo a vulputate accumsan. Sed quis quam ut arcu dictum consequat vel in lacus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Duis at ligula nisl. Aliquam sed volutpat justo.\r\n"
								+ "\r\n"
								+ "Mauris malesuada cursus dolor in placerat. Quisque a interdum ligula. Nunc posuere posuere luctus. Vivamus et velit sit amet leo luctus varius nec sit amet eros. Curabitur nunc nunc, porta non eleifend at, dictum sit amet leo. Mauris porta tempor erat, ut placerat tellus porta porttitor. Suspendisse lacinia odio leo. Donec et nisl tincidunt, lacinia diam nec, aliquet urna. Nulla facilisi.\r\n"
								+ "\r\n"
								+ "Ut porttitor fermentum est nec ornare. Nullam a leo vitae elit efficitur porttitor. Praesent at hendrerit justo. Ut vel congue orci. Donec sit amet felis fermentum, congue tellus vel, tincidunt mi. Aenean id ex tempus, porttitor nunc eu, eleifend quam. Quisque luctus egestas erat sed fermentum. Integer pretium nulla vitae vulputate pellentesque. Nunc eu elit in felis lobortis placerat vel nec sem.\r\n"
								+ "\r\n"
								+ "Sed malesuada sem nec rutrum cursus. Cras ornare justo eu libero pretium pulvinar. Curabitur facilisis pellentesque egestas. Aliquam placerat lorem sollicitudin felis hendrerit, eget auctor massa bibendum. Nam eu dignissim felis, non sodales ante. Fusce ipsum libero, porta ut magna ut, tincidunt faucibus erat. Praesent varius sed leo non vehicula. Integer elementum mollis imperdiet. Maecenas mauris dui, volutpat nec mattis eget, lacinia et nulla. Nullam turpis leo, viverra id quam nec, faucibus cursus justo. Maecenas malesuada porttitor scelerisque. Nam a hendrerit urna. Ut blandit eu leo vel sodales.");

				notificacionesAccordion.getPanes().add(titledPane);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al cargar el archivo FXML");
		}
        
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

        //cargarNotificaciones();
        //cargarRecordatorios(fechaSeleccionada);
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

    // NOTIFICACIONES 
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

    // BOTON VER DIARIO
    private void verDiario() {

        try {
            Stage stage = (Stage) rootPrincipal.getScene().getWindow();
            NavegadorVentanas.navegar("/escenas/diario/VentanaDiarioPersonal.fxml");
            Scene sceneNueva = stage.getScene();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                "No se pudo abrir la pantalla diario: " + ex.getMessage());
        }
    }

    // BOTON VER AMIGOS
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
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                "No se pudo abrir Login: " + ex.getMessage());
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
