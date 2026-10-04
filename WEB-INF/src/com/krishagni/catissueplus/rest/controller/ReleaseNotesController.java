package com.krishagni.catissueplus.rest.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.common.service.CommonService;

@Controller
@RequestMapping("/release-notes")
public class ReleaseNotesController {

	@Autowired
	private CommonService commonService;

	@RequestMapping(method = RequestMethod.GET, value="/latest-summary")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, String> getLatestReleaseNotes() {
		return Collections.singletonMap("notes", commonService.getLatestReleaseNotes());
	}
}
