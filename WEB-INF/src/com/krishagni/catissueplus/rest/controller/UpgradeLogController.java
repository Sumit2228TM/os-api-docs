package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UpgradeLogDetail;
import com.krishagni.catissueplus.core.common.service.UpgradeLogService;

@Controller
@RequestMapping("/upgrade-logs")
public class UpgradeLogController {
	@Autowired
	private UpgradeLogService upgradeLogSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<UpgradeLogDetail> getUpgradeLogs() {
		ResponseEvent<List<UpgradeLogDetail>> resp = upgradeLogSvc.getUpgradeLogs();
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
}
