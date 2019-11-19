package com.lluviadeideas.jpa_mysql.models.dao;

import com.lluviadeideas.jpa_mysql.models.entity.Cliente;

import org.springframework.data.repository.CrudRepository;

//No es necesario poner @Component porque la clase crud ya forma parte de spring
public interface IClienteDao extends CrudRepository<Cliente, Long>{
    

}