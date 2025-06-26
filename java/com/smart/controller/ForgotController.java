package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.service.EmailService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

    private final EmailService emailService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    ForgotController(EmailService emailService) {
        this.emailService = emailService;
    }
	
	@RequestMapping("/forgot")
	public String openEmailForm(Model m) {
		m.addAttribute("title","Forgot Password");
		return "forgot_email_form";
	}
	
	
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("emailotp") String email, Model model,HttpSession session) {
		System.out.println(email);
		
		//set email 
		
		
		//1. generate otp
		Random random = new Random();
		int otp =  random.nextInt(999999);
		System.out.println("otp :"+otp);
		session.setAttribute("system_otp", otp);
		session.setAttribute("email",email);
		
		
		String subject = "Smart Contact Manager - OTP Verification";
		String message = "Your OTP for password reset is: " + otp +" ⚠️ Do not share this OTP with anyone.";

		try {
			emailService.sendEmail(email, subject, message);
			model.addAttribute("title", "Verify OTP");
			model.addAttribute("otp_message", "OTP Send in Your Registered Email");
			return "verify_otp";
			
		} catch (Exception e) {
			model.addAttribute("error","failed ot send OTP,please try Again.");
			return "forgot_email_form";
		
		}
	}
	
	
	
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int user_otp, HttpSession session, Model model) {

		Integer system_otp = (Integer) session.getAttribute("system_otp");
		String email = (String) session.getAttribute("email");

		if (system_otp == null) {
			model.addAttribute("error", "Session expired or OTP not sent. Please try again.");
			model.addAttribute("title", "Forgot Password");
			return "forgot_email_form";
		}

		if (user_otp != system_otp) {
			model.addAttribute("error", "Invalid OTP. Try again.");
			model.addAttribute("title", "Verify OTP");
			return "verify_otp";
		}

		// OTP matched
		User user = this.userRepository.getUserByUserName(email);
		if (user == null) {
			model.addAttribute("mess", "User does not exist. Please enter a registered email.");
			model.addAttribute("title", "Forgot Password");
			return "forgot_email_form";
		}

		// OTP and user both valid
		model.addAttribute("title", "Change Password");
		return "new_password";
	}

	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String Password,HttpSession httpSession) {
		String email = (String) httpSession.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		
		user.setPassword(this.bCryptPasswordEncoder.encode(Password));
		this.userRepository.save(user);
		
		return "redirect:/login?change=Password changed Successfully..";
	}
	
	
}
