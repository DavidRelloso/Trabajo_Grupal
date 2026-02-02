package jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public static byte[] generarInformeUsuarios() throws JRException {

        JasperReport reporte = (JasperReport) JRLoader.loadObject(
                ReportGenerator.class.getResource("/reports/informe_usuarios.jasper")
        );

        List<Usuario> lista = BD.obtenerUsuarios();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

        Map<String, Object> params = new HashMap<>();
        params.put("TITULO", "GRUPI - Listado de Usuarios");

        JasperPrint print = JasperFillManager.fillReport(reporte, params, dataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(print, baos);

        return baos.toByteArray();
    }
}


