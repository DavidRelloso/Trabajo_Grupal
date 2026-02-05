package controller.diary.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.diary.ControladorDiarioPersonal;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControladorColumnaDia {

	@FXML private VBox root;
    @FXML private Label lblTituloDia;
    @FXML private Button btnAddNota;
    @FXML private Button btnEliminarDia;
    @FXML private VBox notasVBox;

    private Long idDia;
    private LocalDate fecha;
    private String categoria;
    
    private ControladorDiarioPersonal padre;

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM");
    

    public void setPadre(ControladorDiarioPersonal padre) { 
           this.padre = padre;
       }


    @FXML
    private void initialize() {
        btnAddNota.setOnAction(e -> onAgregarNota());
        btnEliminarDia.setOnAction(e -> onEliminarDia());
    }

    public void setDatosDia(Long idDia, LocalDate fecha, String categoria) {
        this.idDia = idDia;
        this.fecha = fecha;
        this.categoria = categoria;

        actualizarTitulo();
    }

	public void agregarNota(Node bloqueNota) {
		notasVBox.getChildren().add(bloqueNota);
	}
    
    public VBox getContenedorNotas() { return notasVBox;}
    public Long getIdDia() { return idDia;}
    public LocalDate getFecha() { return fecha;}
    public String getCategoria() { return categoria;}
    public VBox getRoot() { return root; }


    private void actualizarTitulo() {
        if (lblTituloDia == null || fecha == null) return;

        lblTituloDia.setText(
                FECHA_FMT.format(fecha) + " · " + categoria
        );
    }


    private void onAgregarNota() {
          if (padre != null) padre.onAgregarNotaEnDia(idDia, fecha, categoria);
          else System.out.println("Agregar nota en día " + idDia);
      }

      private void onEliminarDia() {
          if (padre != null && root != null) padre.onEliminarDia(idDia, root);
          else System.out.println("Eliminar día " + idDia);
      }
  }

