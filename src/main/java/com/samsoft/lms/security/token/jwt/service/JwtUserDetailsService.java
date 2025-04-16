//package com.samsoft.lms.security.token.jwt.service;
//
//
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class JwtUserDetailsService implements UserDetailsService {
//
//	@Value("{enc-pass}")
//	private String encPass;
//	
////	@Autowired
////	private UserRepository userRepository;
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
//		try {
//			
////			in.fin.main.security.userLogin.entity.User user = userRepository.findByUserId(username);
////			if (user == null) {
////				throw new UsernameNotFoundException("User not found with username: " + username);
////			}
//
//			return new User(username, encPass, new ArrayList<>());
//		} catch (Exception e) {
////			log.error("loadUserByUsername ==>" + e);
//			e.printStackTrace();
//		}
//		return null;
//
//	}
//
//}
