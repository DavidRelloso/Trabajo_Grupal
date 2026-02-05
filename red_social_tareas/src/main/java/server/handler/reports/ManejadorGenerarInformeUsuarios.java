package server.handler.reports;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import server.handler.ManejadorAcciones;
import server.service.user.UsuarioService;
import shared.dto.reports.UsuarioInformeDTO;
import shared.protocol.Respuesta;

public class ManejadorGenerarInformeUsuarios implements ManejadorAcciones<Void> {

    private static final String REPORT_PATH = "reports/informe_usuarios.jasper";

    // Caché del reporte compilado (evita re-leer el recurso y reduce riesgo de locks)
    private static volatile JasperReport REPORT_CACHE;

    private final UsuarioService usuarioService;

    public ManejadorGenerarInformeUsuarios(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Class<Void> payloadType() {
        return Void.class;
    }

    @Override
    public Respuesta handle(Void payload, String usuarioLogueado) {

        if (usuarioLogueado == null) {
            return new Respuesta(false, "Debes iniciar sesión para generar el informe.", null);
        }

        try {
            // 1) Cargar/cachar el .jasper (cerrando el InputStream siempre)
            JasperReport report = getReport();

            // 2) Datos (DTO con getters EXACTOS que pide el .jasper)
            //    Fields del .jasper: id_usuario (Integer), nombre_usuario (String), correo (String), contraseña_hash (String)
            List<UsuarioInformeDTO> usuarios = usuarioService.listarUsuariosParaInforme();

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(usuarios);

            // 3) Parámetros del informe
            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Informe de usuarios");

            // 4) Rellenar informe
            JasperPrint jp = JasperFillManager.fillReport(report, params, ds);

            // 5) Exportar a PDF
            byte[] pdfBytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                JasperExportManager.exportReportToPdfStream(jp, baos);
                pdfBytes = baos.toByteArray();
            }

            return new Respuesta(true, "Informe generado", pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();
            return new Respuesta(false, "Error interno: " + root.getMessage(), null);
        }
    }

    private static JasperReport getReport() throws JRException {
        JasperReport cached = REPORT_CACHE;
        if (cached != null) return cached;

        synchronized (ManejadorGenerarInformeUsuarios.class) {
            cached = REPORT_CACHE;
            if (cached != null) return cached;

            try (InputStream is = ManejadorGenerarInformeUsuarios.class
                    .getClassLoader()
                    .getResourceAsStream(REPORT_PATH)) {

                if (is == null) {
                    throw new IllegalStateException("No se encontró " + REPORT_PATH + " en resources");
                }

                JasperReport loaded = (JasperReport) JRLoader.loadObject(is);
                REPORT_CACHE = loaded;
                return loaded;
            } catch (Exception ex) {
                // Si no es JRException, lo envolvemos para mantener firma
                if (ex instanceof JRException jre) throw jre;
                throw new JRException(ex);
            }
        }
    }
}
