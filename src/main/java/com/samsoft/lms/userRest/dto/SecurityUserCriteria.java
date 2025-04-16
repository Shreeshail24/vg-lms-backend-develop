//package com.samsoft.lms.userRest.dto;
//
//import java.io.Serializable;
//import java.util.List;
//
//import org.hibernate.CacheMode;
//import org.hibernate.Criteria;
//import org.hibernate.FetchMode;
//import org.hibernate.FlushMode;
//import org.hibernate.HibernateException;
//import org.hibernate.LockMode;
//import org.hibernate.ScrollMode;
//import org.hibernate.ScrollableResults;
//import org.hibernate.criterion.Criterion;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projection;
//import org.hibernate.sql.JoinType;
//import org.hibernate.transform.ResultTransformer;
//
//
////@ParameterObject
//@SuppressWarnings("common-java:DuplicatedBlocks")
//public class SecurityUserCriteria implements Serializable, Criteria {
//
//    private static final long serialVersionUID = 1L;
//
//    private LongFilter id;
//
//    private StringFilter firstName;
//
//    private StringFilter lastName;
//
//    private StringFilter designation;
//
//    private StringFilter username;
//
//    private StringFilter passwordHash;
//
//    private StringFilter mobileNo;
//
//    private StringFilter email;
//
//    private BooleanFilter isActivated;
//
//    private LongFilter orgId;
//
//    private LongFilter managerId;
//
//    private LongFilter level;
//
//    private InstantFilter resetDate;
//
//    private StringFilter createdBy;
//
//    private InstantFilter createdOn;
//
//    private InstantFilter lastModified;
//
//    private StringFilter lastModifiedBy;
//
//    private LongFilter securityPermissionId;
//
//    private LongFilter securityRoleId;
//
//    private Boolean distinct;
//
//    public SecurityUserCriteria() {}
//
//    public SecurityUserCriteria(SecurityUserCriteria other) {
//        this.id = other.id == null ? null : other.id.copy();
//        this.firstName = other.firstName == null ? null : other.firstName.copy();
//        this.lastName = other.lastName == null ? null : other.lastName.copy();
//        this.designation = other.designation == null ? null : other.designation.copy();
//        this.username = other.username == null ? null : other.username.copy();
//        this.passwordHash = other.passwordHash == null ? null : other.passwordHash.copy();
//        this.mobileNo = other.mobileNo == null ? null : other.mobileNo.copy();
//        this.email = other.email == null ? null : other.email.copy();
//        this.isActivated = other.isActivated == null ? null : other.isActivated.copy();
//        this.orgId = other.orgId == null ? null : other.orgId.copy();
//        this.managerId = other.managerId == null ? null : other.managerId.copy();
//        this.level = other.level == null ? null : other.level.copy();
//        this.resetDate = other.resetDate == null ? null : other.resetDate.copy();
//        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
//        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
//        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
//        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
//        this.securityPermissionId = other.securityPermissionId == null ? null : other.securityPermissionId.copy();
//        this.securityRoleId = other.securityRoleId == null ? null : other.securityRoleId.copy();
//        this.distinct = other.distinct;
//    }
//
//    @Override
//    public SecurityUserCriteria copy() {
//        return new SecurityUserCriteria(this);
//    }
//
//    public LongFilter getId() {
//        return id;
//    }
//
//    public LongFilter id() {
//        if (id == null) {
//            id = new LongFilter();
//        }
//        return id;
//    }
//
//    public void setId(LongFilter id) {
//        this.id = id;
//    }
//
//    public StringFilter getFirstName() {
//        return firstName;
//    }
//
//    public StringFilter firstName() {
//        if (firstName == null) {
//            firstName = new StringFilter();
//        }
//        return firstName;
//    }
//
//    public void setFirstName(StringFilter firstName) {
//        this.firstName = firstName;
//    }
//
//    public StringFilter getLastName() {
//        return lastName;
//    }
//
//    public StringFilter lastName() {
//        if (lastName == null) {
//            lastName = new StringFilter();
//        }
//        return lastName;
//    }
//
//    public void setLastName(StringFilter lastName) {
//        this.lastName = lastName;
//    }
//
//    public StringFilter getDesignation() {
//        return designation;
//    }
//
//    public StringFilter designation() {
//        if (designation == null) {
//            designation = new StringFilter();
//        }
//        return designation;
//    }
//
//    public void setDesignation(StringFilter designation) {
//        this.designation = designation;
//    }
//
//    public StringFilter getUsername() {
//        return username;
//    }
//
//    public StringFilter username() {
//        if (username == null) {
//            username = new StringFilter();
//        }
//        return username;
//    }
//
//    public void setUsername(StringFilter username) {
//        this.username = username;
//    }
//
//    public StringFilter getPasswordHash() {
//        return passwordHash;
//    }
//
//    public StringFilter passwordHash() {
//        if (passwordHash == null) {
//            passwordHash = new StringFilter();
//        }
//        return passwordHash;
//    }
//
//    public void setPasswordHash(StringFilter passwordHash) {
//        this.passwordHash = passwordHash;
//    }
//
//    public StringFilter getMobileNo() {
//        return mobileNo;
//    }
//
//    public StringFilter mobileNo() {
//        if (mobileNo == null) {
//            mobileNo = new StringFilter();
//        }
//        return mobileNo;
//    }
//
//    public void setMobileNo(StringFilter mobileNo) {
//        this.mobileNo = mobileNo;
//    }
//
//    public StringFilter getEmail() {
//        return email;
//    }
//
//    public StringFilter email() {
//        if (email == null) {
//            email = new StringFilter();
//        }
//        return email;
//    }
//
//    public void setEmail(StringFilter email) {
//        this.email = email;
//    }
//
//    public BooleanFilter getIsActivated() {
//        return isActivated;
//    }
//
//    public BooleanFilter isActivated() {
//        if (isActivated == null) {
//            isActivated = new BooleanFilter();
//        }
//        return isActivated;
//    }
//
//    public void setIsActivated(BooleanFilter isActivated) {
//        this.isActivated = isActivated;
//    }
//
//    public LongFilter getOrgId() {
//        return orgId;
//    }
//
//    public LongFilter orgId() {
//        if (orgId == null) {
//            orgId = new LongFilter();
//        }
//        return orgId;
//    }
//
//    public void setOrgId(LongFilter orgId) {
//        this.orgId = orgId;
//    }
//
//    public LongFilter getManagerId() {
//        return managerId;
//    }
//
//    public LongFilter managerId() {
//        if (managerId == null) {
//            managerId = new LongFilter();
//        }
//        return managerId;
//    }
//
//    public void setManagerId(LongFilter managerId) {
//        this.managerId = managerId;
//    }
//
//    public LongFilter getLevel() {
//        return level;
//    }
//
//    public LongFilter level() {
//        if (level == null) {
//            level = new LongFilter();
//        }
//        return level;
//    }
//
//    public void setLevel(LongFilter level) {
//        this.level = level;
//    }
//
//    public InstantFilter getResetDate() {
//        return resetDate;
//    }
//
//    public InstantFilter resetDate() {
//        if (resetDate == null) {
//            resetDate = new InstantFilter();
//        }
//        return resetDate;
//    }
//
//    public void setResetDate(InstantFilter resetDate) {
//        this.resetDate = resetDate;
//    }
//
//    public StringFilter getCreatedBy() {
//        return createdBy;
//    }
//
//    public StringFilter createdBy() {
//        if (createdBy == null) {
//            createdBy = new StringFilter();
//        }
//        return createdBy;
//    }
//
//    public void setCreatedBy(StringFilter createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public InstantFilter getCreatedOn() {
//        return createdOn;
//    }
//
//    public InstantFilter createdOn() {
//        if (createdOn == null) {
//            createdOn = new InstantFilter();
//        }
//        return createdOn;
//    }
//
//    public void setCreatedOn(InstantFilter createdOn) {
//        this.createdOn = createdOn;
//    }
//
//    public InstantFilter getLastModified() {
//        return lastModified;
//    }
//
//    public InstantFilter lastModified() {
//        if (lastModified == null) {
//            lastModified = new InstantFilter();
//        }
//        return lastModified;
//    }
//
//    public void setLastModified(InstantFilter lastModified) {
//        this.lastModified = lastModified;
//    }
//
//    public StringFilter getLastModifiedBy() {
//        return lastModifiedBy;
//    }
//
//    public StringFilter lastModifiedBy() {
//        if (lastModifiedBy == null) {
//            lastModifiedBy = new StringFilter();
//        }
//        return lastModifiedBy;
//    }
//
//    public void setLastModifiedBy(StringFilter lastModifiedBy) {
//        this.lastModifiedBy = lastModifiedBy;
//    }
//
//    public LongFilter getSecurityPermissionId() {
//        return securityPermissionId;
//    }
//
//    public LongFilter securityPermissionId() {
//        if (securityPermissionId == null) {
//            securityPermissionId = new LongFilter();
//        }
//        return securityPermissionId;
//    }
//
//    public void setSecurityPermissionId(LongFilter securityPermissionId) {
//        this.securityPermissionId = securityPermissionId;
//    }
//
//    public LongFilter getSecurityRoleId() {
//        return securityRoleId;
//    }
//
//    public LongFilter securityRoleId() {
//        if (securityRoleId == null) {
//            securityRoleId = new LongFilter();
//        }
//        return securityRoleId;
//    }
//
//    public void setSecurityRoleId(LongFilter securityRoleId) {
//        this.securityRoleId = securityRoleId;
//    }
//
//    public Boolean getDistinct() {
//        return distinct;
//    }
//
//    public void setDistinct(Boolean distinct) {
//        this.distinct = distinct;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        final SecurityUserCriteria that = (SecurityUserCriteria) o;
//        return (
//            Objects.equals(id, that.id) &&
//            Objects.equals(firstName, that.firstName) &&
//            Objects.equals(lastName, that.lastName) &&
//            Objects.equals(designation, that.designation) &&
//            Objects.equals(username, that.username) &&
//            Objects.equals(passwordHash, that.passwordHash) &&
//            Objects.equals(mobileNo, that.mobileNo) &&
//            Objects.equals(email, that.email) &&
//            Objects.equals(isActivated, that.isActivated) &&
//            Objects.equals(orgId, that.orgId) &&
//            Objects.equals(managerId, that.managerId) &&
//            Objects.equals(level, that.level) &&
//            Objects.equals(resetDate, that.resetDate) &&
//            Objects.equals(createdBy, that.createdBy) &&
//            Objects.equals(createdOn, that.createdOn) &&
//            Objects.equals(lastModified, that.lastModified) &&
//            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
//            Objects.equals(securityPermissionId, that.securityPermissionId) &&
//            Objects.equals(securityRoleId, that.securityRoleId) &&
//            Objects.equals(distinct, that.distinct)
//        );
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(
//            id,
//            firstName,
//            lastName,
//            designation,
//            username,
//            passwordHash,
//            mobileNo,
//            email,
//            isActivated,
//            orgId,
//            managerId,
//            level,
//            resetDate,
//            createdBy,
//            createdOn,
//            lastModified,
//            lastModifiedBy,
//            securityPermissionId,
//            securityRoleId,
//            distinct
//        );
//    }
//
//    // prettier-ignore
//    @Override
//    public String toString() {
//        return "SecurityUserCriteria{" +
//            (id != null ? "id=" + id + ", " : "") +
//            (firstName != null ? "firstName=" + firstName + ", " : "") +
//            (lastName != null ? "lastName=" + lastName + ", " : "") +
//            (designation != null ? "designation=" + designation + ", " : "") +
//            (username != null ? "username=" + username + ", " : "") +
//            (passwordHash != null ? "passwordHash=" + passwordHash + ", " : "") +
//            (mobileNo != null ? "mobileNo=" + mobileNo + ", " : "") +
//            (email != null ? "email=" + email + ", " : "") +
//            (isActivated != null ? "isActivated=" + isActivated + ", " : "") +
//            (orgId != null ? "orgId=" + orgId + ", " : "") +
//            (managerId != null ? "managerId=" + managerId + ", " : "") +
//            (level != null ? "level=" + level + ", " : "") +
//            (resetDate != null ? "resetDate=" + resetDate + ", " : "") +
//            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
//            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
//            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
//            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
//            (securityPermissionId != null ? "securityPermissionId=" + securityPermissionId + ", " : "") +
//            (securityRoleId != null ? "securityRoleId=" + securityRoleId + ", " : "") +
//            (distinct != null ? "distinct=" + distinct + ", " : "") +
//            "}";
//    }
//
//	@Override
//	public String getAlias() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setProjection(Projection projection) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria add(Criterion criterion) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria addOrder(Order order) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setFetchMode(String associationPath, FetchMode mode) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setLockMode(LockMode lockMode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setLockMode(String alias, LockMode lockMode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createAlias(String associationPath, String alias) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createAlias(String associationPath, String alias, JoinType joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createAlias(String associationPath, String alias, JoinType joinType, Criterion withClause)
//			throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createAlias(String associationPath, String alias, int joinType, Criterion withClause)
//			throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, JoinType joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, int joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, String alias) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, String alias, JoinType joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, String alias, int joinType) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, String alias, JoinType joinType, Criterion withClause)
//			throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria createCriteria(String associationPath, String alias, int joinType, Criterion withClause)
//			throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setResultTransformer(ResultTransformer resultTransformer) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setMaxResults(int maxResults) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setFirstResult(int firstResult) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isReadOnlyInitialized() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isReadOnly() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Criteria setReadOnly(boolean readOnly) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setFetchSize(int fetchSize) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setTimeout(int timeout) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setCacheable(boolean cacheable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setCacheRegion(String cacheRegion) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setComment(String comment) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria addQueryHint(String hint) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setFlushMode(FlushMode flushMode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Criteria setCacheMode(CacheMode cacheMode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List list() throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ScrollableResults scroll() throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object uniqueResult() throws HibernateException {
//		// TODO Auto-generated method stub
//		return null;
//	}