//package com.samsoft.lms.security.token.jwt.filter;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.samsoft.lms.security.token.jwt.service.JwtUserDetailsService;
//import com.samsoft.lms.security.token.jwt.util.JwtTokenUtil;
//
//import io.jsonwebtoken.ExpiredJwtException;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter{
//	@Autowired
//	private JwtUserDetailsService jwtUserDetailsService;
//
//	@Autowired
//	private JwtTokenUtil jwtTokenUtil;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//			throws ServletException, IOException,ExpiredJwtException{
//
//		final String requestTokenHeader = request.getHeader("Authorization");
//
//		String username = null;
//		String jwtToken = null;
//		// JWT Token is in the form "Bearer token". Remove Bearer word and get
//		// only the Token
//		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//			jwtToken = requestTokenHeader.substring(7);
//			try {
//				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//			} catch (IllegalArgumentException e) {
//				System.out.println("Unable to get JWT Token");
//			} catch (ExpiredJwtException e) {
//				System.out.println("JWT Token has expired");
//				throw e;
//			}
//		} else {
//			logger.warn("JWT Token does not begin with Bearer String");
//		}
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		// Once we get the token validate it.
//		if (username != null && securityContext.getAuthentication() == null) {
//			
//			System.out.println("inside validate token \n securityContext======>"+securityContext);
//			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
//
//			try {
//				if (jwtTokenUtil.validateToken(jwtToken,username)) {
//					System.out.println("inside validate token");
//					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//							userDetails, null, userDetails.getAuthorities());
//					usernamePasswordAuthenticationToken
//							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//					
//					securityContext.setAuthentication(usernamePasswordAuthenticationToken);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		chain.doFilter(request, response);
//	}
//}
