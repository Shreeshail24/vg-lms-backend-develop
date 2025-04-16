package com.samsoft.lms.userRest.service;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.samsoft.lms.userRest.dto.KeyAndPasswordVM;
import com.samsoft.lms.userRest.dto.LoginUserDTO;
import com.samsoft.lms.userRest.dto.LoginVM;
import com.samsoft.lms.userRest.dto.PasswordChangeDTO;
import com.samsoft.lms.userRest.dto.SecurityUserDTO;

@Service
public class UserRestTamplateService {
	
	@Autowired
	 private  RestTemplate restTemplate;

	    @Value("${usermanagement.service.url}")
	    private String userManagementServiceUrl;

//	    public UserRestTamplateService(RestTemplate restTemplate) {
//	        this.restTemplate = restTemplate;
//	    }

	    public ResponseEntity<String> authenticate(LoginVM loginVM) {
	        String url = userManagementServiceUrl + "/authenticate";
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);

	            HttpEntity<LoginVM> requestEntity = new HttpEntity<>(loginVM, headers);

	            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	        } 
//	        catch (HttpClientErrorException | HttpServerErrorException ex) {
//	        	
//	            throw new RuntimeException("Error calling User Management Service authenticate API: " + ex.getMessage(), ex);
//	        }
	        catch (HttpClientErrorException e) {
	            // Handle 4xx errors (e.g., bad credentials)
	            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                return ResponseEntity
	                    .badRequest()
	                    .body("Failed to sign-in !! You have entered an invalid username or password.");
	            }
	            throw e; // Rethrow if it's not a known error
	        } catch (Exception e) {
	            // Handle unexpected errors
	            return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred while processing the request.");
	        }
	    }

	    public String checkAuthentication(HttpServletRequest request) {
	        String url = userManagementServiceUrl + "/authenticate";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", request.getHeader("Authorization"));

	        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
	        try {
	            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
	            return response.getBody();
	        } catch (Exception ex) {
	            throw new RuntimeException("Error checking authentication: " + ex.getMessage(), ex);
	        }
	    }

	    public ResponseEntity<String> logout(String authHeader, String username) {
	        String urlWithParams = userManagementServiceUrl + "/logout?username=" + username;

	        try {
	            // Prepare headers
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", authHeader);

	            // Prepare request entity
	            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

	            // Make the API call
	            ResponseEntity<String> response = restTemplate.exchange(
	                urlWithParams,
	                HttpMethod.POST,
	                requestEntity,
	                String.class
	            );

	            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
	        } catch (HttpClientErrorException ex) {
	            // Handle 4xx errors
	            return new ResponseEntity<>("Client error: " + ex.getMessage(), ex.getStatusCode());
	        } catch (HttpServerErrorException ex) {
	            // Handle 5xx errors
	            return new ResponseEntity<>("Server error: " + ex.getMessage(), ex.getStatusCode());
	        } catch (Exception ex) {
	            // Handle generic errors
	            return new ResponseEntity<>("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    public LoginUserDTO getAccount(String token) {
	        String url = userManagementServiceUrl + "/account";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token);

	        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
	        try {
	            ResponseEntity<LoginUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LoginUserDTO.class);
	            return response.getBody();
	        } catch (HttpClientErrorException | HttpServerErrorException ex) {
	            throw new RuntimeException("Error fetching account information: " + ex.getMessage(), ex);
	        }
	    }

	    public LoginUserDTO getLoggedInUser(String token) {
	        String url = userManagementServiceUrl + "/loggedInUser";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token);

	        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
	        try {
	            ResponseEntity<LoginUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LoginUserDTO.class);
	            return response.getBody();
	        } catch (HttpClientErrorException | HttpServerErrorException ex) {
	            throw new RuntimeException("Error fetching logged-in user information: " + ex.getMessage(), ex);
	        }
	    }
	    
	    public void changePassword(PasswordChangeDTO passwordChangeDto) {
	        String url = userManagementServiceUrl + "/account/change-password";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<PasswordChangeDTO> requestEntity = new HttpEntity<>(passwordChangeDto, headers);
	        try {
	            restTemplate.postForEntity(url, requestEntity, Void.class);
	        } catch (Exception ex) {
	            throw new RuntimeException("Error calling change-password API: " + ex.getMessage(), ex);
	        }
	    }

	    public void requestPasswordReset(String email) {
	        String url = userManagementServiceUrl + "/account/reset-password/init?mail={email}";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

//	        HttpEntity<String> requestEntity = new HttpEntity<>(email, headers);
	        HttpEntity<Void> requestEntity = new HttpEntity<>(headers); // No body needed

	        try {
//	            restTemplate.postForEntity(url, requestEntity, Void.class);
	        	restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class, email);
	        } catch (Exception ex) {
	            throw new RuntimeException("Error calling reset-password/init API: " + ex.getMessage(), ex);
	        }
	    }

	    public void finishPasswordReset(KeyAndPasswordVM keyAndPassword) {
	        String url = userManagementServiceUrl + "/account/reset-password/finish";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<KeyAndPasswordVM> requestEntity = new HttpEntity<>(keyAndPassword, headers);
	        try {
	            restTemplate.postForEntity(url, requestEntity, Void.class);
	        } catch (Exception ex) {
	            throw new RuntimeException("Error calling reset-password/finish API: " + ex.getMessage(), ex);
	        }
	    }
	    
	    
	    public ResponseEntity<SecurityUserDTO> createSecurityUser(String authHeader,SecurityUserDTO securityUserDTO) {
	        String url = userManagementServiceUrl + "/security-users";
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization",  authHeader);  // Ensure this token is valid
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	        
	        return restTemplate.postForEntity(url, securityUserDTO, SecurityUserDTO.class, requestEntity);
	    }

	    public ResponseEntity<SecurityUserDTO> updateSecurityUser(String authHeader, Long id, SecurityUserDTO securityUserDTO) {
	        String url = userManagementServiceUrl + "/security-users/" + id;
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization",  authHeader);  // Ensure this token is valid
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<SecurityUserDTO> requestEntity = new HttpEntity<>(securityUserDTO);
	        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, SecurityUserDTO.class);
	    }
	    
	    
	    public List<SecurityUserDTO> getAllSecurityUsers(String authHeader, Long securityRoleId, Pageable pageable) {
	        String url = userManagementServiceUrl + "/security-users";

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization",  authHeader);  // Ensure this token is valid
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	        try {
	        // Construct URL with query parameters
	        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
	                .queryParam("securityRoleId.equals", securityRoleId)
	                .queryParam("page", pageable.getPageNumber())
	                .queryParam("size", pageable.getPageSize());

	        // Send GET request with headers using exchange()
	        ResponseEntity<SecurityUserDTO[]> responseEntity = restTemplate.exchange(
	                uriBuilder.toUriString(),
	                HttpMethod.GET,
	                requestEntity,
	                SecurityUserDTO[].class
	        );
	        
	        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
	            throw new RuntimeException("Unauthorized: Invalid token");
	        }

	        return Arrays.asList(responseEntity.getBody());
	        } catch (HttpClientErrorException e) {
	            System.out.println("Error Response: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
	            throw e; // Rethrow for better debugging
	        }
	    }
	    
	    public List<SecurityUserDTO> getAllSecurityUsers(String authHeader, String firstName, Pageable pageable) {
	        String url = userManagementServiceUrl + "/security-users";

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization",  authHeader);  // Ensure this token is valid
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	        try {
	        // Construct URL with query parameters
	        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
	             //   .queryParam("firstName.contains", firstName)
	                .queryParam("pageNo", pageable.getPageNumber())
	                .queryParam("pageSize", pageable.getPageSize());
	        
	        if (firstName != null && !firstName.trim().isEmpty()) {
	          //  uriBuilder.queryParam("firstName.contains", firstName);
	        	uriBuilder.queryParam("firstName", "%" + firstName + "%");

	        }

	        // Send GET request with headers using exchange()
	        ResponseEntity<SecurityUserDTO[]> responseEntity = restTemplate.exchange(
	                uriBuilder.toUriString(),
	                HttpMethod.GET,
	                requestEntity,
	                SecurityUserDTO[].class
	        );
	        
	        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
	            throw new RuntimeException("Unauthorized: Invalid token");
	        }

	        return Arrays.asList(responseEntity.getBody());
	        } catch (HttpClientErrorException e) {
	            System.out.println("Error Response: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
	            throw e; // Rethrow for better debugging
	        }
	    }
	    
	    
	    public ResponseEntity<SecurityUserDTO> getSecurityUser(String authHeader, Long id) {
	        String url = userManagementServiceUrl + "/security-users/" + id;

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authHeader);  // Ensure authHeader contains "Bearer <token>"
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Create HttpEntity with headers
	        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

	        try {
	            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, SecurityUserDTO.class);
	        } catch (HttpClientErrorException e) {
	            System.out.println("Error Response: " + e.getResponseBodyAsString());
	            return ResponseEntity.status(e.getStatusCode()).build();
	        }
	    }

	    public ResponseEntity<Void> deleteSecurityUser(Long id) {
	        String url = userManagementServiceUrl + "/security-users/" + id;
	        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	    }

}
