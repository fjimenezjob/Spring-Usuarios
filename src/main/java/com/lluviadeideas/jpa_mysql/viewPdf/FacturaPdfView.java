package com.lluviadeideas.jpa_mysql.viewPdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lluviadeideas.jpa_mysql.models.entity.Factura;
import com.lluviadeideas.jpa_mysql.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura =(Factura) model.get("factura");
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);
        tabla.addCell("Datos del Cliente");
        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        PdfPTable tablaDos = new PdfPTable(1);
        tablaDos.setSpacingAfter(20);
        tablaDos.addCell("Datos de la Factura");
        tablaDos.addCell("Folio : " +factura.getId());
        tablaDos.addCell("Descripción : " + factura.getDescripcion());
        tablaDos.addCell("Fecha : " + factura.getCreated_At());

        PdfPTable tablaTres = new PdfPTable(4);
        tablaTres.addCell("Producto");
        tablaTres.addCell("Precio");
        tablaTres.addCell("Cantidad");
        tablaTres.addCell("Total");

        for(ItemFactura item : factura.getItems()) {
            tablaTres.addCell(item.getProducto().getNombre());
            tablaTres.addCell(item.getProducto().getPrecio().toString());
            tablaTres.addCell(item.getCantidad().toString());
            tablaTres.addCell(item.calcularImporte().toString());
        }
        
        document.add(tabla);
        document.add(tablaDos);

        PdfPCell cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablaTres.addCell(cell);
        tablaTres.addCell(factura.getTotal().toString());
        document.add(tablaTres);
    }


}