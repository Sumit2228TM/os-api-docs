package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.RoleAccessControl;

public class RoleDetail {
	private Long id;
	
	private String name;
	
	private String description;
	
	private List<RoleAccessControlDetails> acl = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoleAccessControlDetails> getAcl() {
		return acl;
	}

	public void setAcl(List<RoleAccessControlDetails> acl) {
		this.acl = acl;
	}

	public static RoleDetail from(Role role) {
		RoleDetail rs = new RoleDetail();
		rs.setId(role.getId());
		rs.setName(role.getName());
		rs.setDescription(role.getDescription());
		
		for (RoleAccessControl rac : role.getAcl()) {
			rs.getAcl().add(RoleAccessControlDetails.fromRoleAccessControl(rac));
		}
		
		return rs;
	}
	
	public static List<RoleDetail> from(List<Role> roles) {
		return roles.stream().map(RoleDetail::from).collect(Collectors.toList());
	}
}
