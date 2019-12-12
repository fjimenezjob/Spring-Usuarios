package com.lluviadeideas.jpa_mysql.controllers;

import java.util.List;

import com.lluviadeideas.jpa_mysql.models.entity.Cliente;
import com.lluviadeideas.jpa_mysql.models.entity.Factura;
import com.lluviadeideas.jpa_mysql.models.entity.ItemFactura;
import com.lluviadeideas.jpa_mysql.models.entity.Producto;
import com.lluviadeideas.jpa_mysql.models.service.IClienteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model) {

        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);
        model.addAttribute("titulo", "Crear Factura");
        model.addAttribute("factura", factura);
        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByName(term);
    }

    @PostMapping("/form")
    public String guardar(Factura factura, @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, SessionStatus status) {

        for (int i = 0; i < itemId.length; i++) {
            Producto producto = clienteService.finProductoById(itemId[i]);
            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: "+itemId[i].toString()+  " Cantidad: " + cantidad[i].toString());
        }
        
        clienteService.saveFactura(factura);
        status.setComplete();
        return "redirect:/ver/"+factura.getCliente().getId();
    }
}