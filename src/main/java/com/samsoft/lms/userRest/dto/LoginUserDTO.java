package com.samsoft.lms.userRest.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class LoginUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String firstName;

    private String lastName;

    private Long societyId;
    
    private String designation;

    private String username;

    private String mobileNo;

    private String email;

    private byte[] imageUrl;

    private String langKey;

    private String status;

    private String activationKey;

    private String resetKey;

    private Instant resetDate;

    private String createdBy;

    private Instant createdOn;

    private String lastUpdatedBy;

    private Instant lastUpdated;

    private Set<String> authorities;

    private Set<String> roles;

    public LoginUserDTO() {
        // Empty constructor needed for Jackson.
    }

//    public LoginUserDTO(SecurityUser securityUser) {
//        this.id = securityUser.getId();
//        this.firstName = securityUser.getFirstName();
//        this.lastName = securityUser.getLastName();
//
//        this.username = securityUser.getUsername();
//        this.mobileNo = securityUser.getMobileNo();
//        this.email = securityUser.getEmail();
//        //		this.status = securityUser.getStatus();
//        this.resetDate = securityUser.getResetDate();
//        this.createdBy = securityUser.getCreatedBy();
//        this.createdOn = securityUser.getCreatedOn();
//        //        this.authorities = securityUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
//    }

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

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public byte[] getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return this.resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return this.resetDate;
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((langKey == null) ? 0 : langKey.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
        result = prime * result + ((resetDate == null) ? 0 : resetDate.hashCode());
        result = prime * result + ((societyId == null) ? 0 : societyId.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((activationKey == null) ? 0 : activationKey.hashCode());
        result = prime * result + ((resetKey == null) ? 0 : resetKey.hashCode());
        result = prime * result + ((activationKey == null) ? 0 : activationKey.hashCode());
        result = prime * result + ((activationKey == null) ? 0 : activationKey.hashCode());
        result = prime * result + ((designation == null) ? 0 : designation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LoginUserDTO other = (LoginUserDTO) obj;
        if (authorities == null) {
            if (other.authorities != null) return false;
        } else if (!authorities.equals(other.authorities)) return false;
        if (roles == null) {
            if (other.roles != null) return false;
        } else if (!roles.equals(other.roles)) return false;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        if (firstName == null) {
            if (other.firstName != null) return false;
        } else if (!firstName.equals(other.firstName)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (imageUrl == null) {
            if (other.imageUrl != null) return false;
        } else if (!imageUrl.equals(other.imageUrl)) return false;
        if (langKey == null) {
            if (other.langKey != null) return false;
        } else if (!langKey.equals(other.langKey)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        if (societyId == null) {
            if (other.societyId != null) return false;
        } else if (!societyId.equals(other.societyId)) return false;
        if (username == null) {
            if (other.username != null) return false;
        } else if (!username.equals(other.username)) return false;
        if (mobileNo == null) {
            if (other.mobileNo != null) return false;
        } else if (!mobileNo.equals(other.mobileNo)) return false;
        if (resetDate == null) {
            if (other.resetDate != null) return false;
        } else if (!resetDate.equals(other.resetDate)) return false;
        if (designation == null) {
            if (other.designation != null) return false;
        } else if (!designation.equals(other.designation)) return false;
        return true;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginUserDTO{" +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
			", societyId='" + societyId + '\'' +
			", mobileNo='" + mobileNo + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", langKey='" + langKey + '\'' +
			", status='" + status + '\'' +
			", activationKey='" + activationKey + '\'' +
			", resetKey='" + resetKey + '\'' +
			", resetDate='" + resetDate + '\'' +
            ", createdBy=" + createdBy +
            ", createdOn=" + createdOn +
            ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
            ", lastUpdated=" + lastUpdated +
            ", authorities=" + authorities +
            ", roles=" + roles +
            ", designation=" + designation +
            "}";
    }
}