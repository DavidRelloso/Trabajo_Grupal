package controller.diary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import client.Sesion;
import controller.ControladorFuncionesCompartidas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import server.service.notes.VisibilidadNota;
import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.CrearDiaDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ControladorCrearNota extends ControladorFuncionesCompartidas {

    @FXML private GridPane rootCrearNota;

    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private TextArea txtTexto;

    @FXML private Button btnCompartir;
    @FXML private Button btnPublico;
    @FXML private Button btnPrivado;
    @FXML private Button btnGuardar;

    @SuppressWarnings("unused")
	private VisibilidadNota visibilidadSeleccionada = null;
    private String visibilidadSeleccionadaStr = null;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private NotaCreadaListener listener;
    public void setListener(NotaCreadaListener listener) { this.listener = listener; }

    @FXML
    public void initialize() {
        cbCategoria.getItems().setAll("OCIO", "TRABAJO", "ESTUDIO");

        // Botón guardar desactivado hasta que haya estado seleccionado
        btnGuardar.setDisable(true);

        // Selección de visibilidad
        btnCompartir.setOnAction(e -> seleccionarEstado(VisibilidadNota.COMPARTIR));
        btnPublico.setOnAction(e -> seleccionarEstado(VisibilidadNota.PUBLICO));
        btnPrivado.setOnAction(e -> seleccionarEstado(VisibilidadNota.PRIVADO));

        // Guardar 
        btnGuardar.setOnAction(e -> peticionCrearNota());

        // fechas solo hoy en adelante
        dpFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (!empty && date.isBefore(LocalDate.now())) setDisable(true);
            }
        });

        limpiarErrorAlEditar(txtTitulo);
        limpiarErrorAlEditar(txtHoraInicio);
        limpiarErrorAlEditar(txtHoraFin);

        txtTexto.textProperty().addListener((obs, o, n) -> quitarError(txtTexto));
        dpFecha.valueProperty().addListener((obs, o, n) -> quitarError(dpFecha));
        cbCategoria.valueProperty().addListener((obs, o, n) -> quitarError(cbCategoria));
    }

    private void seleccionarEstado(VisibilidadNota visibilidadNota) {
        this.visibilidadSeleccionada = visibilidadNota;
        this.visibilidadSeleccionadaStr = (visibilidadNota == null) ? null : visibilidadNota.name();

        marcarBotonActivo(btnCompartir, visibilidadNota == VisibilidadNota.COMPARTIR);
        marcarBotonActivo(btnPublico, visibilidadNota == VisibilidadNota.PUBLICO);
        marcarBotonActivo(btnPrivado, visibilidadNota == VisibilidadNota.PRIVADO);

        btnGuardar.setDisable(false);
    }

    private void marcarBotonActivo(Button btn, boolean activo) {
        btn.setOpacity(activo ? 1.0 : 0.5);
    }

    // PETICION AL SERVIDOR 
    private void peticionCrearNota() {

        ValidacionResultado resultado = validarFormulario();
        if (!resultado.ok) {
            mostrarAlerta(Alert.AlertType.WARNING, "Formulario incompleto", resultado.mensaje);
            return;
        }

        UserDTO u = Sesion.getUsuario();
        if (u == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Sesión", "No hay usuario logueado.");
            return;
        }

        btnGuardar.setDisable(true);

        // Coger dtos introducidos
        LocalDate fecha = dpFecha.getValue();
        String categoria = cbCategoria.getValue().trim();

        String titulo = txtTitulo.getText().trim();
        String texto = txtTexto.getText().trim();

        LocalTime inicio = LocalTime.parse(txtHoraInicio.getText().trim(), HORA_FMT);

        String finTxt = (txtHoraFin.getText() == null) ? "" : txtHoraFin.getText().trim();
        LocalTime fin = finTxt.isEmpty() ? null : LocalTime.parse(finTxt, HORA_FMT);

        CrearNotaDTO dto = new CrearNotaDTO(
                u.nombreUsuario,
                fecha,
                categoria,
                titulo,
                texto,
                inicio,
                fin,
                visibilidadSeleccionadaStr
        );

        new Thread(() -> {
            try {
                Respuesta resp = enviar(new Peticion("CREAR_NOTA", dto));

                Platform.runLater(() -> {
                    if (!resp.ok) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", resp.message);
                        btnGuardar.setDisable(false);
                        return;
                    }

                    if (!(resp.data instanceof CrearDiaDTO out)) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "Respuesta inválida del servidor.");
                        btnGuardar.setDisable(false);
                        return;
                    }

                    if (listener != null) {
                        listener.onNotaCreada(out, dto);
                    }

                    ((Stage) rootCrearNota.getScene().getWindow()).close();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No conecta: " + e.getMessage());
                    btnGuardar.setDisable(false);
                });
            }
        }).start();
    }

    // VALIDACON 
    private static class ValidacionResultado {
        final boolean ok;
        final String mensaje;

        ValidacionResultado(boolean ok, String mensaje) {
            this.ok = ok;
            this.mensaje = mensaje;
        }
    }

    private ValidacionResultado validarFormulario() {

        boolean ok = true;
        StringBuilder sb = new StringBuilder();

        if (dpFecha.getValue() == null) {
            ok = false; sb.append("• Selecciona una fecha.\n");
            marcarError(dpFecha);
        } else quitarError(dpFecha);

        if (cbCategoria.getValue() == null || cbCategoria.getValue().trim().isEmpty()) {
            ok = false; sb.append("• Selecciona una categoría.\n");
            marcarError(cbCategoria);
        } else quitarError(cbCategoria);

        if (txtTitulo.getText() == null || txtTitulo.getText().trim().isEmpty()) {
            ok = false; sb.append("• El título no puede estar vacío.\n");
            marcarError(txtTitulo);
        } else quitarError(txtTitulo);

        LocalTime inicio = null;
        String inicioTxt = (txtHoraInicio.getText() == null) ? "" : txtHoraInicio.getText().trim();
        if (inicioTxt.isEmpty()) {
            ok = false; sb.append("• La hora de inicio es obligatoria.\n");
            marcarError(txtHoraInicio);
        } else {
            try {
                inicio = LocalTime.parse(inicioTxt, HORA_FMT);
                quitarError(txtHoraInicio);
            } catch (DateTimeParseException ex) {
                ok = false;
                sb.append("• Hora inicio inválida. Usa HH:mm (ej: 09:30).\n");
                marcarError(txtHoraInicio);
            }
        }

        LocalTime fin = null;
        String finTxt = (txtHoraFin.getText() == null) ? "" : txtHoraFin.getText().trim();
        if (!finTxt.isEmpty()) {
            try {
                fin = LocalTime.parse(finTxt, HORA_FMT);
                quitarError(txtHoraFin);
            } catch (DateTimeParseException ex) {
                ok = false;
                sb.append("• Hora fin inválida. Usa HH:mm (ej: 18:00).\n");
                marcarError(txtHoraFin);
            }
            if (inicio != null && fin != null && fin.isBefore(inicio)) {
                ok = false;
                sb.append("• La hora fin no puede ser anterior a la hora inicio.\n");
                marcarError(txtHoraFin);
            }
        } else quitarError(txtHoraFin);

        if (txtTexto.getText() == null || txtTexto.getText().trim().isEmpty()) {
            ok = false;
            sb.append("• El texto no puede estar vacío.\n");
            marcarError(txtTexto);
        } else quitarError(txtTexto);

        if (visibilidadSeleccionadaStr == null || visibilidadSeleccionadaStr.isBlank()) {
            ok = false;
            sb.append("• Selecciona un estado de publicación.\n");
        }

        return new ValidacionResultado(ok, sb.toString().trim());
    }

    private void marcarError(Control c) {
        c.setStyle("-fx-border-color: #D64545; -fx-border-width: 2px; -fx-border-radius: 10px;");
    }

    private void quitarError(Control c) {
        c.setStyle("");
    }

    private void limpiarErrorAlEditar(TextInputControl c) {
        c.textProperty().addListener((obs, o, n) -> quitarError(c));
    }

    @SuppressWarnings("unused")
    private void limpiarFormulario() {
        dpFecha.setValue(null);
        cbCategoria.getSelectionModel().clearSelection();
        txtTitulo.clear();
        txtHoraInicio.clear();
        txtHoraFin.clear();
        txtTexto.clear();

        visibilidadSeleccionada = null;
        visibilidadSeleccionadaStr = null;

        marcarBotonActivo(btnCompartir, false);
        marcarBotonActivo(btnPublico, false);
        marcarBotonActivo(btnPrivado, false);

        btnGuardar.setDisable(true);
    }
}
