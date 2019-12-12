package com.lluviadeideas.jpa_mysql.models.dao;

import com.lluviadeideas.jpa_mysql.models.entity.Factura;

import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long >{

}