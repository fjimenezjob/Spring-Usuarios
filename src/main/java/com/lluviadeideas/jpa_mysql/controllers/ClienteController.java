package com.lluviadeideas.jpa_mysql.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import com.lluviadeideas.jpa_mysql.models.entity.Cliente;
import com.lluviadeideas.jpa_mysql.models.service.IClienteService;
import com.lluviadeideas.jpa_mysql.models.service.IUploadFileService;
import com.lluviadeideas.jpa_mysql.util.paginator.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    private final static String UPLOADS_FOLDER = "uploads";

    @Autowired
    IUploadFileService uploadFileService;

    @GetMapping(value = "/uploads/{filename:.+}") // El ":.+" lo que hace es cojer la extensión del archivo (nombre de
                                                  // la foto + extensión).
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("subtitulo", "Listado de Clientes de la web");
        model.addAttribute("clientes", clientes);
        model.addAttribute("footer", "Footer de la pagina");
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @GetMapping("/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario Cliente Registro");
        model.put("subtitulo", "Formularo de inscripción");
        model.put("footer", "Footer de la pagina");
        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
        } else {
            return "redirect:listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
            @RequestParam("file") MultipartFile foto, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario Cliente Registro");
            return "form";
        }

        if (!foto.isEmpty()) {
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto().length() > 0) {
                uploadFileService.delete(cliente.getFoto());
            }
            String uniqueFileName = null;
            try {
                uniqueFileName = uploadFileService.copy(foto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cliente.setFoto(uniqueFileName);
        }

        clienteService.save(cliente);
        status.setComplete();
        return "redirect:/listar";
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);

        if (cliente == null) {
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle Cliente: " + cliente.getNombre());
        return "ver";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);

            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
            File archivo = rootPath.toFile();

            if (archivo.exists() && archivo.canRead()) {
                uploadFileService.delete(cliente.getFoto());
            }
        }
        return "redirect:/listar";
    }
}