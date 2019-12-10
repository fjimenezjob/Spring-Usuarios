package com.lluviadeideas.jpa_mysql.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lluviadeideas.jpa_mysql.models.dao.IClienteDao;
import com.lluviadeideas.jpa_mysql.models.dao.IProductoDao;
import com.lluviadeideas.jpa_mysql.models.entity.Cliente;
import com.lluviadeideas.jpa_mysql.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);

    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {

        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByName(String term) {

        return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
    }
}