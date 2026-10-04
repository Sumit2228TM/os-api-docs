package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.krishagni.catissueplus.core.biospecimen.events.ConsentStatementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentStatementListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.ConsentStatementService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/consent-statements")
public class ConsentStatementsController {
	
	@Autowired
	private ConsentStatementService consentStmtsSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConsentStatementDetail> getStatements(
		@RequestParam(value="code", required = false)
		String code,
		
		@RequestParam(value="statement", required = false)
		String statement,

		@RequestParam(value = "searchString", required = false)
		String searchString,

		@RequestParam(value = "startAt", required = false, defaultValue = "0")
		int startAt,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
		int maxResults) {

		ConsentStatementListCriteria crit = new ConsentStatementListCriteria()
			.code(code)
			.statement(statement)
			.query(searchString)
			.startAt(startAt)
			.maxResults(maxResults);
		
		RequestEvent<ConsentStatementListCriteria> req = new RequestEvent<>(crit);
		ResponseEvent<List<ConsentStatementDetail>> resp = consentStmtsSvc.getStatements(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Long> getStatementsCount(
		@RequestParam(value = "code", required = false)
		String code,
			
		@RequestParam(value="statement", required = false)
		String statement,

		@RequestParam(value = "searchString", required = false)
		String searchString) {
		
		ConsentStatementListCriteria crit = new ConsentStatementListCriteria()
			.code(code)
			.statement(statement)
			.query(searchString);
		
		RequestEvent<ConsentStatementListCriteria> req = new RequestEvent<>(crit);
		ResponseEvent<Long> resp = consentStmtsSvc.getStatementsCount(req);
		resp.throwErrorIfUnsuccessful();
		return Collections.singletonMap("count", resp.getPayload());
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ConsentStatementDetail getStatement(@PathVariable("id") Long id) {
		RequestEvent<EntityQueryCriteria> req = new RequestEvent<>(new EntityQueryCriteria(id));
		ResponseEvent<ConsentStatementDetail> resp = consentStmtsSvc.getStatement(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentStatementDetail createStatement(@RequestBody ConsentStatementDetail detail) {
		RequestEvent<ConsentStatementDetail> req = new RequestEvent<>(detail);
		ResponseEvent<ConsentStatementDetail> resp = consentStmtsSvc.createStatement(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentStatementDetail updateStatement(
		@PathVariable("id")
		Long id,
		
		@RequestBody
		ConsentStatementDetail detail) {

		detail.setId(id);
		RequestEvent<ConsentStatementDetail> req = new RequestEvent<>(detail);
		ResponseEvent<ConsentStatementDetail> resp = consentStmtsSvc.updateStatement(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

}
