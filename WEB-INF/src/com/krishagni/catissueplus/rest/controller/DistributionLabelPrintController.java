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

import com.krishagni.catissueplus.core.administrative.events.PrintDistributionLabelDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.common.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.common.events.LabelTokenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/distribution-label-printer")
public class DistributionLabelPrintController extends AbstractLabelPrinter {

	@Autowired
	private DistributionOrderService orderSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/tokens")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<LabelTokenDetail> getPrintTokens() {
		ResponseEvent<List<LabelTokenDetail>> resp = orderSvc.getPrintLabelTokens();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public LabelPrintJobSummary printLabels(@RequestBody PrintDistributionLabelDetail detail) {
		RequestEvent<PrintDistributionLabelDetail> req = new RequestEvent<>(detail);
		ResponseEvent<LabelPrintJobSummary> resp = orderSvc.printDistributionLabels(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}