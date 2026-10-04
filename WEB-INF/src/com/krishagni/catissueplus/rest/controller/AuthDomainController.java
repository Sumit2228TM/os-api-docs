
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.auth.events.AuthDomainDetail;
import com.krishagni.catissueplus.core.auth.events.AuthDomainSummary;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/auth-domains")
public class AuthDomainController {

	@Autowired
	private DomainRegistrationService domainRegSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<AuthDomainSummary> getDomains(
		@RequestParam(value = "maxResults", required = false, defaultValue = "1000")
		int maxResults) {
		
		ListAuthDomainCriteria crit = new ListAuthDomainCriteria().maxResults(maxResults);
		return ResponseEvent.unwrap(domainRegSvc.getDomains(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail getDomain(@PathVariable("id") Long domainId) {
		AuthDomainSummary crit = new AuthDomainSummary();
		crit.setId(domainId);
		return ResponseEvent.unwrap(domainRegSvc.getDomain(RequestEvent.wrap(crit)));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail registerDomain(@RequestBody AuthDomainDetail domain) {
		return ResponseEvent.unwrap(domainRegSvc.registerDomain(RequestEvent.wrap(domain)));
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail updateDomain(@PathVariable("id") Long id, @RequestBody AuthDomainDetail domain) {
		domain.setId(id);
		return ResponseEvent.unwrap(domainRegSvc.updateDomain(RequestEvent.wrap(domain)));
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuthDomainDetail deleteDomain(@PathVariable("id") Long id) {
		AuthDomainSummary domain = new AuthDomainSummary();
		domain.setId(id);
		return ResponseEvent.unwrap(domainRegSvc.deleteDomain(RequestEvent.wrap(domain)));
	}
}
