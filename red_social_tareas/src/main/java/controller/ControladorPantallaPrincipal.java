package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

public class ControladorPantallaPrincipal {

    @FXML private Accordion notificacionesAccordion; 
    @FXML private Accordion recordatoriosAccordion;
    @FXML private Button btnVerDiario, btnVerAmigos, btnVerAjustes, btnCerrarSesion, btnGenerarJasper;
    
    @FXML
    public void initialize() {
        //agregarFXMLAlAccordion();
    	configurarBotones();
    	agregarNoti();
    }
    
    public void agregarNoti() {
        try {
			
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/componentesReusables/ComponenteNotificacionAmigo.fxml"));
            Parent nuevoComponente = loader.load();  

            TitledPane titledPane = (TitledPane) nuevoComponente;
            /*ControladorNotificaciones controller = loader.getController();
            controller.setInfoNoti("NombreAmigo", "TEXTO_NOTIFICACION");
            controller.setInfoNoti("amigo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent elementum urna ac feugiat convallis. In consectetur mollis dolor id mollis. Nunc ipsum eros, auctor id felis vitae, ultricies eleifend nibh. Aliquam erat volutpat. Donec hendrerit ex sapien. Curabitur ligula enim, mattis id lacus id, posuere suscipit ipsum. Mauris a odio eu ante condimentum ornare vel a mauris. Nulla facilisis velit quis justo faucibus vehicula. Maecenas felis velit, hendrerit non tempor a, cursus non risus.\r\n"
            		+ "\r\n"
            		+ "Donec venenatis, diam egestas gravida convallis, velit nulla blandit ligula, in suscipit ante sapien vitae est. Integer eget eleifend enim, eu euismod lorem. Sed luctus ante a mollis facilisis. Mauris facilisis nibh eget vehicula faucibus. Fusce sed erat eros. Ut eleifend euismod vulputate. Nullam feugiat libero a luctus elementum. Vivamus quis rutrum urna, feugiat malesuada magna. Etiam nec finibus orci, non eleifend odio. Donec scelerisque justo a vulputate accumsan. Sed quis quam ut arcu dictum consequat vel in lacus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Duis at ligula nisl. Aliquam sed volutpat justo.\r\n"
            		+ "\r\n"
            		+ "Mauris malesuada cursus dolor in placerat. Quisque a interdum ligula. Nunc posuere posuere luctus. Vivamus et velit sit amet leo luctus varius nec sit amet eros. Curabitur nunc nunc, porta non eleifend at, dictum sit amet leo. Mauris porta tempor erat, ut placerat tellus porta porttitor. Suspendisse lacinia odio leo. Donec et nisl tincidunt, lacinia diam nec, aliquet urna. Nulla facilisi.\r\n"
            		+ "\r\n"
            		+ "Ut porttitor fermentum est nec ornare. Nullam a leo vitae elit efficitur porttitor. Praesent at hendrerit justo. Ut vel congue orci. Donec sit amet felis fermentum, congue tellus vel, tincidunt mi. Aenean id ex tempus, porttitor nunc eu, eleifend quam. Quisque luctus egestas erat sed fermentum. Integer pretium nulla vitae vulputate pellentesque. Nunc eu elit in felis lobortis placerat vel nec sem.\r\n"
            		+ "\r\n"
            		+ "Sed malesuada sem nec rutrum cursus. Cras ornare justo eu libero pretium pulvinar. Curabitur facilisis pellentesque egestas. Aliquam placerat lorem sollicitudin felis hendrerit, eget auctor massa bibendum. Nam eu dignissim felis, non sodales ante. Fusce ipsum libero, porta ut magna ut, tincidunt faucibus erat. Praesent varius sed leo non vehicula. Integer elementum mollis imperdiet. Maecenas mauris dui, volutpat nec mattis eget, lacinia et nulla. Nullam turpis leo, viverra id quam nec, faucibus cursus justo. Maecenas malesuada porttitor scelerisque. Nam a hendrerit urna. Ut blandit eu leo vel sodales.");
             */
            
            notificacionesAccordion.getPanes().add(titledPane);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el archivo FXML");
        }
    }

    public void agregarRecordatorio() {
    	
    }
    
    
    private void configurarBotones() {

        btnVerDiario.setOnAction(e -> {         
            VistaYControlador<Object> vc = cargarVista("prueba.fxml");
            if (vc == null) return;
            Parent vista = vc.getVista();
            btnVerDiario.getScene().setRoot(vista);
        });

        // 👉 AÑADIR ESTA LÍNEA
        btnGenerarJasper.setOnAction(e -> generarInformeJasper());
    }

    
    public <T> VistaYControlador<T> cargarVista(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            T controlador = loader.getController();
            return new VistaYControlador<>(vista, controlador);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class VistaYControlador<T> {
        private final Parent vista;
        private final T controlador;

        public VistaYControlador(Parent vista, T controlador) {
            this.vista = vista;
            this.controlador = controlador;
        }

        public Parent getVista() { return vista; }
        public T getControlador() { return controlador; }
    }
    
    private void generarInformeJasper() {

        javafx.concurrent.Task<Void> tarea = new javafx.concurrent.Task<>() {
            @Override
            protected Void call() throws Exception {

                // Llamada al servidor
                byte[] pdf = Cliente.ClienteTCP.solicitarInforme();

                // Guardar PDF
                java.io.FileOutputStream fos = new java.io.FileOutputStream("informe_usuarios.pdf");
                fos.write(pdf);
                fos.close();

                return null;
            }
        };

        tarea.setOnSucceeded(ev -> {
            System.out.println("Informe generado correctamente");
            abrirPDF("informe_usuarios.pdf");
        });

        tarea.setOnFailed(ev -> {
            System.out.println("Error al generar el informe");
            tarea.getException().printStackTrace();
        });

        new Thread(tarea).start();
    }
    
    private void abrirPDF(String ruta) {
        try {
            java.awt.Desktop.getDesktop().open(new java.io.File(ruta));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

