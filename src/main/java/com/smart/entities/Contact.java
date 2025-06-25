package com.smart.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contact {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int cID;
		private String name;
		private String email;
		private String phone;
		private String work;
		private String image;
		
		private User user;
		
		
		public int getcID() {
			return cID;
		}
		public void setcID(int cID) {
			this.cID = cID;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getWork() {
			return work;
		}
		public void setWork(String work) {
			this.work = work;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		
		
		public Contact() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return "Contact [cID=" + cID + ", name=" + name + ", email=" + email + ", phone=" + phone + ", work=" + work
					+ ", image=" + image + "]";
		}
		
		
	
		
}
