package com.samsoft.lms.userRest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.userRest.dto.KeyAndPasswordVM;
import com.samsoft.lms.userRest.dto.LoginUserDTO;
import com.samsoft.lms.userRest.dto.LoginVM;
import com.samsoft.lms.userRest.dto.PasswordChangeDTO;
import com.samsoft.lms.userRest.dto.SecurityUserDTO;
import com.samsoft.lms.userRest.service.UserRestTamplateService;

@RestController
@RequestMapping("/api/user-restservice")
public class LmsUserRestController {
	
	private final UserRestTamplateService userRestTamplateService;

    public LmsUserRestController(UserRestTamplateService userRestTamplateService) {
        this.userRestTamplateService = userRestTamplateService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@Valid @RequestBody LoginVM loginVM) {
        return userRestTamplateService.authenticate(loginVM);
    }

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        return userRestTamplateService.checkAuthentication(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @RequestBody LoginVM loginVM) {
    	String authHeader = request.getHeader("Authorization");
    	return userRestTamplateService.logout(authHeader, loginVM.getUsername());
    }
    
    @GetMapping("/account")
    public ResponseEntity<LoginUserDTO> getAccount(@RequestHeader("Authorization") String token) {
        LoginUserDTO account = userRestTamplateService.getAccount(token.replace("Bearer ", ""));
        return ResponseEntity.ok(account);
    }

    @GetMapping("/loggedInUser")
    public ResponseEntity<LoginUserDTO> getUserAccount(@RequestHeader("Authorization") String token) {
        LoginUserDTO loggedInUser = userRestTamplateService.getLoggedInUser(token.replace("Bearer ", ""));
        return ResponseEntity.ok(loggedInUser);
    }
    
    @PostMapping("/account/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDto) {
    	userRestTamplateService.changePassword(passwordChangeDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestParam("email") String email) {
    	userRestTamplateService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@Valid @RequestBody KeyAndPasswordVM keyAndPassword) {
    	userRestTamplateService.finishPasswordReset(keyAndPassword);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/security-users")
    public ResponseEntity<SecurityUserDTO> createSecurityUser(HttpServletRequest request, @Valid @RequestBody SecurityUserDTO securityUserDTO) {
       
    	String authHeader = request.getHeader("Authorization");
    	return userRestTamplateService.createSecurityUser(authHeader,securityUserDTO);
    }

    @PutMapping("/security-users/{id}")
    public ResponseEntity<SecurityUserDTO> updateSecurityUser(HttpServletRequest request,
        @PathVariable(value = "id") Long id,
        @Valid @RequestBody SecurityUserDTO securityUserDTO
    ) {
    	String authHeader = request.getHeader("Authorization");
        return userRestTamplateService.updateSecurityUser(authHeader, id, securityUserDTO);
    }

   
    
    @GetMapping("/security-users")
    public ResponseEntity<List<SecurityUserDTO>> getAllSecurityUsers(HttpServletRequest request,
            @RequestParam("securityRoleId") Long securityRoleId,
            Pageable pageable) {
    	String authHeader = request.getHeader("Authorization");
        List<SecurityUserDTO> users = userRestTamplateService.getAllSecurityUsers(authHeader, securityRoleId, pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/security-users/by-firstname")
    public ResponseEntity<List<SecurityUserDTO>> getAllSecurityUsers(HttpServletRequest request,
    		 @RequestParam(value = "firstName", required = false) String firstName,
            Pageable pageable) {
    	String authHeader = request.getHeader("Authorization");
        List<SecurityUserDTO> users = userRestTamplateService.getAllSecurityUsers(authHeader,firstName, pageable);
        return ResponseEntity.ok(users);
    }
//
//    @GetMapping("/security-users/count")
//    public ResponseEntity<Long> countSecurityUsers(SecurityUserCriteria criteria) {
//        return userRestTamplateService.countSecurityUsers(criteria);
//    }

    @GetMapping("/security-users/{id}")
    public ResponseEntity<SecurityUserDTO> getSecurityUser(HttpServletRequest request, @PathVariable Long id) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResponseEntity<SecurityUserDTO> userObj = userRestTamplateService.getSecurityUser(authHeader, id);

        return userObj;
    }

    
    
    
    @DeleteMapping("/security-users/{id}")
    public ResponseEntity<Void> deleteSecurityUser(@PathVariable Long id) {
        return userRestTamplateService.deleteSecurityUser(id);
    }

}
