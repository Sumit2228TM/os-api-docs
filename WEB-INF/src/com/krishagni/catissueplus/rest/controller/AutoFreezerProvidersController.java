package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.domain.AutoFreezerProviderDetail;
import com.krishagni.catissueplus.core.administrative.services.AutoFreezerProviderService;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/auto-freezer-providers")
public class AutoFreezerProvidersController {
	@Autowired
	private AutoFreezerProviderService autoFreezerProviderSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<AutoFreezerProviderDetail> getProviders() {
		return response(autoFreezerProviderSvc.getProviders());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AutoFreezerProviderDetail getProvider(@PathVariable("id") Long providerId) {
		return response(autoFreezerProviderSvc.getProvider(request(new EntityQueryCriteria(providerId))));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AutoFreezerProviderDetail registerProvider(@RequestBody AutoFreezerProviderDetail input) {
		return response(autoFreezerProviderSvc.registerProvider(request(input)));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AutoFreezerProviderDetail updateProvider(
		@PathVariable("id")
		Long providerId,

		@RequestBody
		AutoFreezerProviderDetail input) {

		input.setId(providerId);
		return response(autoFreezerProviderSvc.updateProvider(request(input)));
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
