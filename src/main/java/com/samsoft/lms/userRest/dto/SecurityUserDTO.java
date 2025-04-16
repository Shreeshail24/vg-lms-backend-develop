package com.samsoft.lms.userRest.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class SecurityUserDTO {
	
	 private Long id;

	    private String firstName;

	    private String lastName;

	    private String designation;

	    @NotNull
	    private String username;

	    @NotNull
	    private String passwordHash;

	    private String mobileNo;

	    private String email;

	    private Boolean isActivated;

	    private Long orgId;

	    private Long managerId;

	    private Long level;

	    private Instant resetDate;

	    private String createdBy;

	    private Instant createdOn;

	    private Instant lastModified;

	    private String lastModifiedBy;

	    private String activationKey;

	    private String resetKey;


	    private Set<SecurityRoleDTO> securityRoles = new HashSet<>();

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getDesignation() {
	        return designation;
	    }

	    public void setDesignation(String designation) {
	        this.designation = designation;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPasswordHash() {
	        return passwordHash;
	    }

	    public void setPasswordHash(String passwordHash) {
	        this.passwordHash = passwordHash;
	    }

	    public String getMobileNo() {
	        return mobileNo;
	    }

	    public void setMobileNo(String mobileNo) {
	        this.mobileNo = mobileNo;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public Boolean getIsActivated() {
	        return isActivated;
	    }

	    public void setIsActivated(Boolean isActivated) {
	        this.isActivated = isActivated;
	    }

	    public Long getOrgId() {
	        return orgId;
	    }

	    public void setOrgId(Long orgId) {
	        this.orgId = orgId;
	    }

	    public Long getManagerId() {
	        return managerId;
	    }

	    public void setManagerId(Long managerId) {
	        this.managerId = managerId;
	    }

	    public Long getLevel() {
	        return level;
	    }

	    public void setLevel(Long level) {
	        this.level = level;
	    }

	    public Instant getResetDate() {
	        return resetDate;
	    }

	    public void setResetDate(Instant resetDate) {
	        this.resetDate = resetDate;
	    }

	    public String getCreatedBy() {
	        return createdBy;
	    }

	    public void setCreatedBy(String createdBy) {
	        this.createdBy = createdBy;
	    }

	    public Instant getCreatedOn() {
	        return createdOn;
	    }

	    public void setCreatedOn(Instant createdOn) {
	        this.createdOn = createdOn;
	    }

	    public Instant getLastModified() {
	        return lastModified;
	    }

	    public void setLastModified(Instant lastModified) {
	        this.lastModified = lastModified;
	    }

	    public String getLastModifiedBy() {
	        return lastModifiedBy;
	    }

	    public void setLastModifiedBy(String lastModifiedBy) {
	        this.lastModifiedBy = lastModifiedBy;
	    }

	    public String getActivationKey() {
	        return activationKey;
	    }

	    public void setActivationKey(String activationKey) {
	        this.activationKey = activationKey;
	    }

	    public String getResetKey() {
	        return resetKey;
	    }

	    public void setResetKey(String resetKey) {
	        this.resetKey = resetKey;
	    }


	    public Set<SecurityRoleDTO> getSecurityRoles() {
	        return securityRoles;
	    }

	    public void setSecurityRoles(Set<SecurityRoleDTO> securityRoles) {
	        this.securityRoles = securityRoles;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (!(o instanceof SecurityUserDTO)) {
	            return false;
	        }

	        SecurityUserDTO securityUserDTO = (SecurityUserDTO) o;
	        if (this.id == null) {
	            return false;
	        }
	        return Objects.equals(this.id, securityUserDTO.id);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(this.id);
	    }

	    // prettier-ignore
	    @Override
	    public String toString() {
	        return "SecurityUserDTO{" +
	            "id=" + getId() +
	            ", firstName='" + getFirstName() + "'" +
	            ", lastName='" + getLastName() + "'" +
	            ", designation='" + getDesignation() + "'" +
	            ", username='" + getUsername() + "'" +
	            ", passwordHash='" + getPasswordHash() + "'" +
	            ", mobileNo='" + getMobileNo() + "'" +
	            ", email='" + getEmail() + "'" +
	            ", isActivated='" + getIsActivated() + "'" +
	            ", orgId=" + getOrgId() +
	            ", managerId=" + getManagerId() +
	            ", level=" + getLevel() +
	            ", resetDate='" + getResetDate() + "'" +
	            ", createdBy='" + getCreatedBy() + "'" +
	            ", createdOn='" + getCreatedOn() + "'" +
	            ", lastModified='" + getLastModified() + "'" +
	            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
	            ", securityRoles=" + getSecurityRoles() +
	            ", activationKey='" + getActivationKey() + "'" +
	            ", resetKey='" + getResetKey() + "'" +
	            "}";
	    }

}
