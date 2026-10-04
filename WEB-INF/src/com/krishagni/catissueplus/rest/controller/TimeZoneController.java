package com.krishagni.catissueplus.rest.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/time-zones")
public class TimeZoneController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Map<String, String>> getTimeZones() {
		return Stream.of(TimeZone.getAvailableIDs()).map(TimeZone::getTimeZone)
			.sorted(Comparator.comparingInt(TimeZone::getRawOffset))
			.map(tz -> {
				int offset = tz.getRawOffset() / (60 * 1000);
				String sign = offset < 0 ? "-" : (offset > 0 ? "+" : " ");
				offset = Math.abs(offset);
				int hours =  offset / 60;
				int minutes = offset - hours * 60;

				Map<String, String> tzDetail = new HashMap<>();
				tzDetail.put("id", tz.getID());
				tzDetail.put("name", String.format("%s (%s%02d:%02d)", tz.getID(), sign, hours, minutes));
				return tzDetail;
			}).collect(Collectors.toList());
	}
}
