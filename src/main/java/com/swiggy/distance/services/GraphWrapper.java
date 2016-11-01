package com.swiggy.distance.services;

import static org.apache.commons.lang3.StringUtils.isBlank;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.shapes.BBox;
import com.swiggy.distance.common.City;
import com.swiggy.distance.requests.Point;
import com.swiggy.distance.responses.Element;
import com.swiggy.distance.responses.Status;

@Slf4j
@Setter
public class GraphWrapper {

	private int cityId;
	private GraphHopper graphHopper;
	private String osmPbfFile;
	private String graphDirectory;
	private String vehicleType;
	private boolean chEnable;
	private String chWeighting;
		
	public void loadGraph() {
		log.info("Loading data for cityId : {}", cityId);
		checkConfig();
		graphHopper = new GraphHopper()
			.setOSMFile(osmPbfFile)
			.setCHEnable(chEnable)
			//.setTraversalMode(TraversalMode.NODE_BASED)
			.setGraphHopperLocation(graphDirectory)
			.setEncodingManager(new EncodingManager(vehicleType))
			.setCHWeighting(chWeighting)
			.forServer();
		graphHopper.importOrLoad();
		log.info("Loaded data for cityId : {}", cityId);
	}

	public Element getDistance(Point from, Point to) {
		GHRequest req = new GHRequest(from.getLat(), from.getLng(), to.getLat(), to.getLng())
			.setWeighting("shortest")
			.setVehicle("motorcycle")
		    .setAlgorithm(AlgorithmOptions.ASTAR_BI);
		GHResponse resp = graphHopper.route(req);
		graphHopper.getGraphHopperStorage().getBounds();
		if (resp.hasErrors()) {
			log.error("Graph Hopper error response for cityId : {}, : {}", cityId, resp.getErrors());
			return new Element(Status.ERROR.getCode());
		} else {
			log.debug("Distance for from : {} , to : {}  is {}", from, to , resp.getDistance());
			return new Element(Status.SUCCESS.getCode(), resp.getDistance());
		}
	}
	
	private void checkConfig() {
		if (City.findByValue(cityId) == null) {
			throw new IllegalArgumentException("City id " + cityId + "is invalid");
		}
		if (isBlank(osmPbfFile)) {
			throw new IllegalArgumentException("osmPbfFile is blank or empty");
		}
		if (isBlank(graphDirectory)) {
			throw new IllegalArgumentException("graphDirectory is blank or empty");
		}
	}

	
	public int getCityId() {
		return cityId;
	}

	public boolean canHandle(Point from, Point to) {
		BBox bbox = graphHopper.getGraphHopperStorage().getBounds();
		if (bbox.contains(from.getLat(), from.getLng()) 
				&& bbox.contains(to.getLat(), to.getLng())) {
			return true;
		}
		return false;
	}
	
	public boolean canHandle(Point from) {
		BBox bbox = graphHopper.getGraphHopperStorage().getBounds();
		if (bbox.contains(from.getLat(), from.getLng())) {
			return true;
		}
		return false;
	}
}
