package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRolesList {	
	public final static String ALL_CURRENT_AND_FUTURE = "All Current and Future";

	private Long subjectId;
	
	private String emailAddress;
	
	private List<Role> roles;
	
	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public static SubjectRolesList from(Long userId, String emailId, Collection<SubjectRole> subjectRoles) {
		SubjectRolesList result = new SubjectRolesList();
		result.setSubjectId(userId);
		result.setEmailAddress(emailId);
		
		List<Role> rolesList = new ArrayList<Role>();
		for (SubjectRole subjectRole : subjectRoles) {
			Role roleDetail = new Role();
			roleDetail.setId(subjectRole.getId());
			roleDetail.setRoleName(subjectRole.getRole().getName());

			if (subjectRole.getCollectionProtocol() != null) {
				roleDetail.setCpShortTitle(subjectRole.getCollectionProtocol().getShortTitle());
			} else {
				roleDetail.setCpShortTitle(ALL_CURRENT_AND_FUTURE);
			}

			if (subjectRole.getSite() != null) {
				roleDetail.setSiteName(subjectRole.getSite().getName());
			} else {
				roleDetail.setSiteName(ALL_CURRENT_AND_FUTURE);
			}

			rolesList.add(roleDetail);
		}
		
		result.setRoles(rolesList);		
		return result;
	}
	
	public static class Role {
		private Long id;
		
		private String roleName;
		
		private String siteName;
		
		private String cpShortTitle;
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

		public String getSiteName() {
			return siteName;
		}

		public void setSiteName(String siteName) {
			this.siteName = siteName;
		}

		public String getCpShortTitle() {
			return cpShortTitle;
		}

		public void setCpShortTitle(String cpShortTitle) {
			this.cpShortTitle = cpShortTitle;
		}		
	}
}
