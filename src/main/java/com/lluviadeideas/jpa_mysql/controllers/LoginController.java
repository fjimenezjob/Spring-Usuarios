package com.lluviadeideas.jpa_mysql.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
        Model model, Principal principal) {

        if(principal != null) {
            return "redirect:/";
        }

        if(error != null){
            model.addAttribute("error", "Ha habido un error, comprueba tu usuario y contraseña o contacta con un administrador.");
        }
        
        model.addAttribute("titulo", "Login");
        model.addAttribute("subtitulo" , "Por favor, Inicie sesión.");
        return "login";
    }

}