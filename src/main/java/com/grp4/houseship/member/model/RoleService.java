package com.grp4.houseship.member.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}
	
	public Role findById(int id) {
		Optional<Role> role = roleRepository.findById(id);
		if(role.isPresent()) {
			return role.get();
		}
		return null;
	}
	

}
