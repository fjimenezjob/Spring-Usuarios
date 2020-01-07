package com.lluviadeideas.jpa_mysql.xlsxView;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lluviadeideas.jpa_mysql.models.entity.Factura;

import org.apache.poi.ss.usermodel.Cell;
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

        sheet.createRow(3).createCell(0).setCellValue(messageSource.getMessage("text.cliente.email", null, locale) + ": " + factura.getCliente().getEmail());

        sheet.createRow(3).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datosFactura", null, locale)+ ": ");
        sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.ver.folio", null, locale) + ": " + factura.getId());
        sheet.createRow(5).createCell(0).setCellValue(messageSource.getMessage("text.ver.desc", null, locale) + ": " + factura.getDescripcion());
        sheet.createRow(6).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.fecha", null, locale) + ": " + factura.getCreated_At());
        
    }
    
}