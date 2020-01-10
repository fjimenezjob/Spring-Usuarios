package com.lluviadeideas.jpa_mysql.controllers;

import com.lluviadeideas.jpa_mysql.models.service.IClienteService;
import com.lluviadeideas.jpa_mysql.view.viewXml.ClienteList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping(value ="/listar")
    public ClienteList listar() {
        return new ClienteList(clienteService.findAll());
    }

}