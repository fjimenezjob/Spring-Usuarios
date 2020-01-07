package com.lluviadeideas.jpa_mysql.viewPdf;

import java.awt.Color;
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
        Factura factura = (Factura) model.get("factura");
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase("Datos del Cliente"));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla.addCell(cell);
        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        PdfPTable tablaDos = new PdfPTable(1);
        tablaDos.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase("Datos de la Factura"));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        tablaDos.addCell(cell);
        tablaDos.addCell("Folio : " + factura.getId());
        tablaDos.addCell("Descripción : " + factura.getDescripcion());
        tablaDos.addCell("Fecha : " + factura.getCreated_At());

        PdfPTable tablaTres = new PdfPTable(4);
        tablaTres.setWidths(new float [] {2.5f, 1, 1, 1});
        tablaTres.addCell("Producto");
        tablaTres.addCell("Precio");
        tablaTres.addCell("Cantidad");
        tablaTres.addCell("Total");

        for (ItemFactura item : factura.getItems()) {
            tablaTres.addCell(item.getProducto().getNombre());
            tablaTres.addCell(item.getProducto().getPrecio().toString());
            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tablaTres.addCell(cell);
            tablaTres.addCell(item.calcularImporte().toString());
        }

        document.add(tabla);
        document.add(tablaDos);

        PdfPCell cell1 = new PdfPCell(new Phrase("Total : "));
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablaTres.addCell(cell1);
        tablaTres.addCell(factura.getTotal().toString());
        document.add(tablaTres);
    }

}