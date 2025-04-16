package com.samsoft.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
//	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//	@Autowired
//	private UserDetailsService jwtUserDetailsService;

//	@Autowired
//	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable().cors().and()
				// dont authenticate this particular request
				.authorizeRequests().
//				antMatchers(AUTH_WHITELIST).permitAll().
				// all other requests need to be authenticated
//				anyRequest().authenticated().and().
				antMatchers("/**").permitAll().and();
		// make sure we use stateless session; session won't be used to
		// store user's state.
//				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
//		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		// configure AuthenticationManager so that it knows from where to load
//		// user for matching credentials
//		// Use BCryptPasswordEncoder
//		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//	}

	private static final String[] AUTH_WHITELIST = {
			// -- Swagger UI v2
			"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui.html", "/webjars/**",
			// -- Swagger UI v3 (OpenAPI)
			"/v3/api-docs/**", "/swagger-ui/**", "/TwoFAuth/generate2FAQR/{username}", "/TwoFAuth/validate2FACode",
			"/TwoFAuth/twoFAuthSecretKeyExists/{username}", "/validateUser", "/authenticate", "/refreshToken",
			"/userExists", "/validateAccessToken",

//            TODO : public api for communication
//            "/amort/getAmort",
//			"/amort/getEmi",
//			"/interface/agreementBoarding",
//	    "/lmsapi/interface/agreementBoarding",
			"/encryption/mobileNo", "/encryption/gst", "/encryption/name", "/encryption/aadhaar", "/encryption/address",
			"/encryption/email", "/banking/idfc/callback-va-validation", "/banking/idfc/callback-insta-alert"
			// other public endpoints of your API may be appended to this array
	};

	@Configuration
	public class CorsConfiguration {
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE")
							.allowedOrigins("https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in","https://qa-los.4fin.in","http://10.0.153.25:80","http://10.0.153.25","http://10.0.153.25:9090", "https://qa-losone.4fin.in", "https://fintech-lms.4fin.in","https://yarnbizqalms.4fin.in","http://yarnbizqalms.4fin.in", "https://localhost:8010","http://localhost:4200", "http://dev.techvgi.com","http://qa.techvgi.com")
							.allowedHeaders("*")
							.allowCredentials(true)
					        .exposedHeaders("X-Total-Count", "X-Total-Pages", "X-Current-Page", "X-Page-Size"); // Expose custom headers
				}
			};
		}
	}
}