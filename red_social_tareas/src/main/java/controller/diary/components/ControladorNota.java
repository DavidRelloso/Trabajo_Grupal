package controller.diary.components;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import shared.dto.notes.CrearNotaDTO;

public class ControladorNota {

    @FXML private Label lblTitulo;
    @FXML private Label lblCategoria;
    @FXML private Label lblHora;
    @FXML private Label lblTexto;
    @FXML private Button btnEliminar;

    private Long idNota;
    private Long idDia;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        if (btnEliminar != null) {
            btnEliminar.setOnAction(e -> onEliminar());
        }
    }

    public void setDatosNota(Long idDia, Long idNota, CrearNotaDTO dto) {
        this.idDia = idDia;
        this.idNota = idNota;

        if (lblTitulo != null) lblTitulo.setText(safe(dto.titulo));
        if (lblCategoria != null) lblCategoria.setText(safe(dto.categoria));
        if (lblTexto != null) lblTexto.setText(safe(dto.texto));

        if (lblHora != null) {
            lblHora.setText(formatHora(dto.horaInicio, dto.horaFin));
        }
    }

    private String formatHora(LocalTime inicio, LocalTime fin) {
        if (inicio == null) return "";
        String hIni = HORA_FMT.format(inicio);
        if (fin == null) return hIni;
        return hIni + " - " + HORA_FMT.format(fin);
    }

    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private void onEliminar() {
        System.out.println("Eliminar nota idNota=" + idNota + " (idDia=" + idDia + ")");
        // Aquí:
        // 1) enviar petición "ELIMINAR_NOTA" con idNota
        // 2) si ok, eliminar el nodo de UI (card.getParent().getChildren().remove(card))
    }

    public Long getIdNota() { return idNota; }
    public Long getIdDia() { return idDia; }
}
