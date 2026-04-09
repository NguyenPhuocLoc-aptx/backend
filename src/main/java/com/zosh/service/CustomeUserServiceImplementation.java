package com.zosh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zosh.model.User;
import com.zosh.repository.UserRepository;

@Service
public class CustomeUserServiceImplementation implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomeUserServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email - " + username));


		String role = user.getRole() != null ? user.getRole().name() : "USER";

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), authorities);
	}
}