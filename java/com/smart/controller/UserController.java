package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final DaoAuthenticationProvider authenticationProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;


    UserController(DaoAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
	
	
	//commman data 
	@ModelAttribute
	public void addCommanData(Model model,Principal principal) {
		String username = principal.getName();
		System.out.println("USER : " + username );
		
		User user = userRepository.getUserByUserName(username);
		System.out.println(user);
		
		model.addAttribute("user",user);
		
	}
	
	
	//for dashboard 
	@RequestMapping("/dashboard")
	public String deshboard(Model model,Principal principal) {
		
		User currentUser = this.userRepository.getUserByUserName(principal.getName());
		
		model.addAttribute("title","Dashboard..");
		long total = contactRepository.countContactsByUser(currentUser.getId());
		model.addAttribute("totalContacts", total);

		return "normal/user_dashboard";
	}
	
	
	
	//user profile
	@GetMapping("/profile")
	public String profile(Model model ) {
		model.addAttribute("title","Profile..");
		return "normal/user_profile";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//open add form handler
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add Contacts..");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	
	//add_contact from handler(process add contact)
	@PostMapping("/process_contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileimage")MultipartFile file 
			,Principal principal,HttpSession session) {
		
		try {
			
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			//processing  and  uploading file
			if(file.isEmpty()) {
				//print message on UI
				contact.setImage("default_img.png");
				System.out.println("image is not upladed");
			}
			else {
					//file uploading in folder and set name in dB
					contact.setImage(file.getOriginalFilename());
				
					File saveFile = new ClassPathResource("/static/img").getFile();
				 
					Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator + file.getOriginalFilename()) ;
				 
					Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
					System.out.println("image is uploaded");
				 
					//successfully messagezz
					session.setAttribute("message",new Message("Your Contact is Successfully Added ","alert-success"));
				}
			
			contact.setUser(user);
			
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println(contact);
			System.out.println("contact added");
		
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				
				// error messages
				session.setAttribute("message",new Message("Failed..! Try Again","alert-danger"));
				
			}
			return "normal/add_contact_form";
	}
	
	
	//show all user on dashboard....showcontactHandler
	@GetMapping("/all_contact")
	public String getAllContact(Model m,Principal principal, HttpSession session) {
		m.addAttribute("title","All Contacts..");
		//contact's list send on anather page...
		String username =  principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		
		List<Contact> contacts =  this.contactRepository.findContactsByUser(user.getId());
		m.addAttribute("contacts",contacts);
		

//	    // Remove message from session manually
//	    session.removeAttribute("message");
		
		return "normal/user_all_contacts";
	}
	
	
	
	 
	//showing perticular contact
	@RequestMapping("/contact/{cID}")
	public String showContact(@PathVariable("cID") Integer cID,Model m,HttpSession session,Principal principal) {
		
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cID);
		Contact contact  = contactOptional.get();		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		if(user.getId() == contact.getUser().getId())
		{
			m.addAttribute("contact",contact);
			m.addAttribute("title",contact.getName());
		}
	
		System.out.println(cID);
		return "normal/contact_detail";
	}
	
	
	//delete Contact by Contact Id from the user
	@GetMapping("/delete/{cID}")
	public String deleteContact(@PathVariable("cID") Integer cID,Principal principal ,HttpSession httpSession){
		
		Optional<Contact> conOptional = this.contactRepository.findById(cID);
		Contact contact = conOptional.get();
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		
		if(user.getId() == contact.getUser().getId()){
			this.contactRepository.delete(contact);
			httpSession.setAttribute("d_message", new Message("Contect Deleted Successfully! ✅","success"));
		}else {
			httpSession.setAttribute("d_message", new Message("You are Not Authorized to Delete This Contact! ❌","danger"));			
		}
		
		return "redirect:/user/all_contact";
	}
	
	
	//open update form
	@PostMapping("/edit_contact/{cID}")
	public String updateContact(@PathVariable("cID") Integer cid,Model model,Principal principal) {
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		Contact contact = this.contactRepository.findById(cid).get();
		
		if(user.getId() == contact.getUser().getId()) {
			model.addAttribute("title","update contact");
			model.addAttribute("contact" , contact);
			return "normal/update_contact_f";
		}else {
			return "redirect:/user/all_contact";
		}
	}
	
	
	//update form contact handler
	@PostMapping("/update_process")
	public String updatehandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,HttpSession session) {
		
		Contact oldconContact = this.contactRepository.findById(contact.getcID()).get();
		
		
		
		try {
			if(!file.isEmpty()) {
				
				File deleteFile = new ClassPathResource("/static/img").getFile();
				File file1 = new File(deleteFile,oldconContact.getImage());
				file1.delete();
				
				
				
				
				File saveFile = new ClassPathResource("/static/img").getFile();
				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator + file.getOriginalFilename()) ;
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				System.out.println("image is updated..");
			}
			else {
				contact.setImage(oldconContact.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return "redirect:/user/contact/"+contact.getcID();
	}
	
	
	
	
	
	//for setting andler
	@GetMapping("/settings")
	public String openSetting(Model m) {
		m.addAttribute("title","Settings..");
		return "normal/settings";
	}
	
	//change passwaord
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldpassword,
	                             @RequestParam("newPassword") String newpassword,
	                             Principal principal,
	                             HttpSession session,
	                             Model model) {

	    User user = this.userRepository.getUserByUserName(principal.getName());

	    if (this.bCryptPasswordEncoder.matches(oldpassword, user.getPassword())) {
	        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
	        this.userRepository.save(user);
	        session.setAttribute("message_p", new Message("Password changed successfully", "success"));
	        return "redirect:/user/settings"; // ✅ Success goes to full reload
	    } else {
	        session.setAttribute("message_p", new Message("Incorrect old password!", "danger"));
	        model.addAttribute("modalOpen", true); // ✅ key to open modal
	        model.addAttribute("title", "Settings");
	        return "normal/settings"; // ✅ Direct view return
	    }
	}

	
	
}

















