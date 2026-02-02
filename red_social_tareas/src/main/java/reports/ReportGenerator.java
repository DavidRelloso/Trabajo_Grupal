package reports;

import reports.Persona;
import java.util.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportGenerator {

    public static void generarInforme() throws JRException {

        // 1. Cargar el archivo .jasper desde resources
        JasperReport reporte = (JasperReport) JRLoader.loadObject(
                ReportGenerator.class.getResource("/main/reports/informe.jasper")
        );

        // 2. Crear lista de personas con tus atributos reales
        List<Persona> lista = Arrays.asList(
                new Persona(1, "Aritz", "aritz@test.com", "1234"/* "/main/images/avatar.png" */),
                new Persona(2, "Lucía", "lucia@test.com", "abcd"/* "/main/images/avatar.png" */),
                new Persona(3, "Markel", "markel@test.com", "qwerty"/* "/main/images/avatar.png" */)
        );

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

        // 3. Parámetros del informe
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO", "Listado de Usuarios");

        // 4. Rellenar informe
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, dataSource);

        // 5. Exportar a PDF
        JasperExportManager.exportReportToPdfFile(print, "reports/salida.pdf");

        System.out.println("PDF generado correctamente en reports/salida.pdf");
    }
}
