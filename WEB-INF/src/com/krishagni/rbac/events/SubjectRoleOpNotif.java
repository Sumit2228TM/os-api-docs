package com.krishagni.rbac.events;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.rbac.domain.SubjectRole;

public class SubjectRoleOpNotif {
	private List<User> admins;

	private User user;

	private SubjectRole role;

	private Map<String, Object> oldSrDetails;

	private String endUserOp;

	private String roleOp;

	private String subjectNotifMsg;

	private Object[] subjectNotifParams;

	private String adminNotifMsg;

	private Object[] adminNotifParams;

	public List<User> getAdmins() {
		return admins;
	}

	public void setAdmins(List<User> admins) {
		this.admins = admins;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SubjectRole getRole() {
		return role;
	}

	public void setRole(SubjectRole role) {
		this.role = role;
	}

	public Map<String, Object> getOldSrDetails() {
		return oldSrDetails;
	}

	public void setOldSrDetails(Map<String, Object> oldSrDetails) {
		this.oldSrDetails = oldSrDetails;
	}

	public String getEndUserOp() {
		return endUserOp;
	}

	public void setEndUserOp(String endUserOp) {
		this.endUserOp = endUserOp;
	}

	public String getRoleOp() {
		return roleOp;
	}

	public void setRoleOp(String roleOp) {
		this.roleOp = roleOp;
	}

	public String getSubjectNotifMsg() {
		return subjectNotifMsg;
	}

	public void setSubjectNotifMsg(String subjectNotifMsg) {
		this.subjectNotifMsg = subjectNotifMsg;
	}

	public Object[] getSubjectNotifParams() {
		return subjectNotifParams;
	}

	public void setSubjectNotifParams(Object[] subjectNotifParams) {
		this.subjectNotifParams = subjectNotifParams;
	}

	public String getAdminNotifMsg() {
		return adminNotifMsg;
	}

	public void setAdminNotifMsg(String adminNotifMsg) {
		this.adminNotifMsg = adminNotifMsg;
	}

	public Object[] getAdminNotifParams() {
		return adminNotifParams;
	}

	public void setAdminNotifParams(Object[] adminNotifParams) {
		this.adminNotifParams = adminNotifParams;
	}
}
