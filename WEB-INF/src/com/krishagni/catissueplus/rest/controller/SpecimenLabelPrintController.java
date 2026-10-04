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

import com.krishagni.catissueplus.core.biospecimen.events.PrintSpecimenLabelDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.common.events.LabelTokenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/specimen-label-printer")
public class SpecimenLabelPrintController extends AbstractLabelPrinter {
	
	@Autowired
	private SpecimenService specimenSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/tokens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<LabelTokenDetail> getPrintTokens() {
		ResponseEvent<List<LabelTokenDetail>> resp = specimenSvc.getPrintLabelTokens();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printLabel(@RequestBody PrintSpecimenLabelDetail detail) {
		RequestEvent<PrintSpecimenLabelDetail> req = new RequestEvent<>(detail);
		ResponseEvent<LabelPrintJobSummary> resp = specimenSvc.printSpecimenLabels(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
