package com.swiggy.distance.controllers;

import javax.servlet.http.HttpServletRequest;

import com.swiggy.distance.requests.DistanceTupleRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swiggy.distance.requests.DistanceRequest;
import com.swiggy.distance.responses.DistanceResponse;
import com.swiggy.distance.services.DistanceService;

@RestController
@RequestMapping("/")
@Slf4j(topic = "req-res-logger")
public class DistanceController {

	@Autowired
	private DistanceService distanceService;
	
	@RequestMapping(value = "/distance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public DistanceResponse getDistance(@RequestBody DistanceRequest distanceRequest, HttpServletRequest request) {
		log.info("Distance Request : {}", distanceRequest);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceRequest);
		return distanceResponse;
	}

	@RequestMapping(value = "/distance/tuples", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public DistanceResponse getDistanceTuples(@RequestBody DistanceTupleRequest distanceTupleRequest, HttpServletRequest request) {
		log.info("Distance Request : {}", distanceTupleRequest);
		DistanceResponse distanceResponse = distanceService.getDistanceResponse(distanceTupleRequest);
		return distanceResponse;
	}
}
