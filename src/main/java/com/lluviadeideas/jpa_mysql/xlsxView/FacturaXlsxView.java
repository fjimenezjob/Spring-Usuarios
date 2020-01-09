package com.lluviadeideas.jpa_mysql.xlsxView;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lluviadeideas.jpa_mysql.models.entity.Factura;
import com.lluviadeideas.jpa_mysql.models.entity.ItemFactura;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
                
        Locale locale = localeResolver.resolveLocale(request);
        Factura factura = (Factura) model.get("factura");
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.datosCliente", null, locale));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.factura", null, locale) + ": " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.cliente.email", null, locale) + ": " + factura.getCliente().getEmail());

        sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datosFactura", null, locale)+ ": ");
        sheet.createRow(5).createCell(0).setCellValue(messageSource.getMessage("text.ver.folio", null, locale) + ": " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue(messageSource.getMessage("text.ver.desc", null, locale) + ": " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.fecha", null, locale) + ": " + factura.getCreated_At());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);


        
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.producto", null, locale));
        header.createCell(1).setCellValue(messageSource.getMessage("text.factura.ver.precio", null, locale));
        header.createCell(2).setCellValue(messageSource.getMessage("text.factura.ver.cantidad", null, locale));
        header.createCell(3).setCellValue(messageSource.getMessage("text.ver.total", null, locale));

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        int rownum = 10;
        for(ItemFactura item : factura.getItems()){
            Row fila = sheet.createRow(rownum ++);
            cell = fila.createCell(0);
            fila.createCell(0).setCellValue(item.getProducto().getNombre());
            fila.createCell(1).setCellValue(item.getProducto().getPrecio());
            fila.createCell(2).setCellValue(item.getCantidad());
            fila.createCell(3).setCellValue(item.calcularImporte());
        }

        Row filatotal = sheet.createRow(rownum ++);
        filatotal.createCell(2).setCellValue(messageSource.getMessage("text.ver.total", null, locale));
        filatotal.createCell(3).setCellValue(factura.getTotal());
    }
    
}