package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}
	
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About-Samrt Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model m,HttpSession session) {
		m.addAttribute("title","Signup");
		m.addAttribute("user",new User());
		
		//clear the message alert
		session.removeAttribute("message");
		return "signup";
	}
	
	
	//registration handler
	@PostMapping("/do_register")
	public String handle_register(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue = "false")boolean agreement,
			Model m,HttpSession session) {
		
		try {
			
			if(!agreement) {
				System.out.println("you have not agreed the term and condition...!");
				throw new Exception("you have not agreed the term and condition...!");
			}
			
			if(result1.hasErrors()) {
				System.out.println("error"+ result1.toString());
				m.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result = this.userRepository.save(user);
			
			m.addAttribute("user", new User());
			
			session.setAttribute("message",new Message("Successfully Registered !!","alert-success"));
			System.out.println("User : "+user);
			return "signup";
			
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went Wrong !!" + e.getMessage(), "alert-danger"));
			return "signup";
		}
		
	}
	
		
	@RequestMapping("/login")
	public String customLogin(Model m) {
		m.addAttribute("title","Login");
		return "login";
	}
	
}
