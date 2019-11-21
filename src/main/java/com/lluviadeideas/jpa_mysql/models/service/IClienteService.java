package com.lluviadeideas.jpa_mysql.models.service;

import java.util.List;

import com.lluviadeideas.jpa_mysql.models.entity.Cliente;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


public interface IClienteService {
    
    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);
}