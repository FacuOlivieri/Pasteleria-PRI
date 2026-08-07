package com.pasteleriaPri.Pasteleria.controller.rest;

import com.pasteleriaPri.Pasteleria.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pasteleria/email")
public class EmailRestController {

    @Autowired
    private IEmailService emailService;


}
