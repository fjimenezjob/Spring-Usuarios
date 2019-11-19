package com.lluviadeideas.jpa_mysql.models.dao;

import java.util.List;

import com.lluviadeideas.jpa_mysql.models.entity.Cliente;

public interface IClienteDao {

    public List<Cliente> findAll();

    public void save(Cliente cliente);

    public Cliente findOne(Long id);
}