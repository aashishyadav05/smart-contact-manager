package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	//pegination method .....
	
	@Query("from Contact as d where d.user.id = :userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
	
	@Query("SELECT COUNT(c) FROM Contact c WHERE c.user.id = :userId")
	public long countContactsByUser(@Param("userId") int userId);

	//for search
	public List<Contact> findByNameContainingAndUser(String keywords,User user);
	
}
