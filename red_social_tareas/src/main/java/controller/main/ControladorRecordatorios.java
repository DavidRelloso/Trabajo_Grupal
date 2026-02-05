package controller.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class ControladorRecordatorios {

	@FXML
	private Button btnBorrarRecordatorio;
	@FXML
	private TitledPane tpPrincipalRecordatorio;
	@FXML
	private Label lblTextoRecordatorio;

	private Runnable onEliminar;

	private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

	@FXML
	private void initialize() {
		if (btnBorrarRecordatorio != null) {
			btnBorrarRecordatorio.setOnAction(e -> {
				if (onEliminar != null)
					onEliminar.run();
			});
		}
	}

	public void setInfoRecordatorio(String titulo, LocalDate fecha, LocalTime hora, String texto) {
		String head = buildHeader(titulo, fecha, hora);
		tpPrincipalRecordatorio.setText(head);
		lblTextoRecordatorio.setText(texto != null ? texto : "");
	}

	public void setOnEliminar(Runnable onEliminar) {
		this.onEliminar = onEliminar;
	}

	private String buildHeader(String titulo, LocalDate fecha, LocalTime hora) {
		String f = (fecha != null) ? fecha.toString() : "";
		String h = (hora != null) ? hora.format(HORA_FMT) : "";
		return joinDot(titulo, f, h);
	}

	private String joinDot(String a, String b, String c) {
        StringBuilder sb = new StringBuilder();
        append(sb, a);
        append(sb, b);
        append(sb, c);
        return sb.length() == 0 ? "" : sb.toString();
    }

	private void append(StringBuilder sb, String part) {
		if (part == null)
			return;
		String p = part.trim();
		if (p.isEmpty())
			return;

		if (sb.length() > 0)
			sb.append(" · ");
		sb.append(p);
	}
}
