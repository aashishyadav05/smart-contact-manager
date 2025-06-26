package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.smart.entities.EmailRequest;
import com.smart.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/welcome")
    public String welcome() {
    	return "welcome";
    }
    
    
    
    
    
    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendEmail(request.getTo(), request.getSubject(), request.getMessage());
            return "✅ Email sent successfully to: " + request.getTo();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error sending email: " + e.getMessage();
        }
    }
}
