/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.hash.printitext;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author david
 */
public class PrintiText {

    private final static Logger LOGGER = Logger.getLogger("mx.hash.printitext.PrintiText");

    static public void main(String[] args) {
        try {
            PrintiText printer = new PrintiText();

            ByteArrayOutputStream documentoBytes = printer.crearDocumentoiText();
            printer.imprimir(documentoBytes);

        } catch (IOException | PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Error de impresion", "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crea un documento via la libreria iText y lo almacena como un
     * ByteArrayOutputStream
     *
     * @return Documento iText en formato ByteArrayOutputStream
     */
    public ByteArrayOutputStream crearDocumentoiText() {
        LOGGER.log(Level.INFO, "Creando documento iText");

        // Es en este ByteArrayOutputStream donde se pone el documento una vez 
        // que se llama a documento.close()
        ByteArrayOutputStream documentoBytes = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(documentoBytes);
        PdfDocument pdfDoc = new PdfDocument(pdfWriter);

        Document documento = new Document(pdfDoc, PageSize.LETTER);
        LOGGER.log(Level.INFO, "Agregando los elementos al documento");

        documento.add(new Paragraph("Inicia el reporte"));
        documento.add(this.crearTabla());

        documento.close();

        return documentoBytes;
    }

    private Table crearTabla() {
        LOGGER.log(Level.INFO, "Creando tabla");

        float[] anchos = {50F, 50F, 50F};
        Table tablaEncabezado = new Table(anchos);

        tablaEncabezado.setWidth(500F);

        tablaEncabezado.addCell("Hora Inicio");
        tablaEncabezado.addCell("Hora Fin");
        tablaEncabezado.addCell("");
        tablaEncabezado.addCell("Fecha Inicio");
        tablaEncabezado.addCell("Fecha Fin");
        tablaEncabezado.addCell("Fin de Turno");

        return tablaEncabezado;
    }

    /**
     * Envia a imprimir el ByteArrayOutoutStream creado de un documento iText
     *
     * @param documentoBytes
     * @throws IOException
     * @throws PrinterException
     */
    public void imprimir(ByteArrayOutputStream documentoBytes) throws IOException, PrinterException {
        LOGGER.log(Level.INFO, "Enviando documento a imprimir");

        // Aqui convertimos la el arreglo de salida a uno de entrada que podemos
        // mandar a la impresora
        ByteArrayInputStream bais = new ByteArrayInputStream(documentoBytes.toByteArray());
        
        // Creamos un PDDocument con el arreglo de entrada que creamos        
        PDDocument document = PDDocument.load(bais);

        PrinterJob job = PrinterJob.getPrinterJob();

        LOGGER.log(Level.INFO, "Mostrando el dialogo de impresion");
        if (job.printDialog() == true) {
            job.setPageable(new PDFPageable(document));

            LOGGER.log(Level.INFO, "Imprimiendo documento");
            job.print();
        }
    }
}
