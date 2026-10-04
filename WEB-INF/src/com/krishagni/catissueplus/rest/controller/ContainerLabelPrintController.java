package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.PrintContainerLabelDetail;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.common.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.common.events.LabelTokenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/container-label-printer")
public class ContainerLabelPrintController extends AbstractLabelPrinter {

	@Autowired
	private StorageContainerService containerSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/tokens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<LabelTokenDetail> getPrintTokens() {
		ResponseEvent<List<LabelTokenDetail>> resp = containerSvc.getPrintLabelTokens();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printLabels(@RequestBody PrintContainerLabelDetail detail) {
		ResponseEvent<LabelPrintJobSummary> resp = containerSvc.printContainerLabels(new RequestEvent<>(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}