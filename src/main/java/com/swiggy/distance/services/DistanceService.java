package com.swiggy.distance.services;


import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.swiggy.distance.requests.DistanceTupleRequest;
import com.swiggy.distance.requests.Tuples;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.swiggy.distance.common.GraphConfig;
import com.swiggy.distance.requests.DistanceRequest;
import com.swiggy.distance.requests.Point;
import com.swiggy.distance.responses.DistanceResponse;
import com.swiggy.distance.responses.Element;
import com.swiggy.distance.responses.Row;
import com.swiggy.distance.responses.Status;

@Setter
@Slf4j
@Service
public class DistanceService {

	private List<GraphWrapper> graphWrappers = new ArrayList<GraphWrapper>();
	
	private List<GraphConfig> graphConfigs;
	
	@Value("${graphConfigJson}")
	private String graphConfigJson;
		
	@PostConstruct
	public void loadGraphs() {
		populateGraphConfig();
		checkArgument(isNotEmpty(graphConfigs), "graphConfigs cannot be null or empty");
		for (GraphConfig config : graphConfigs) {
			GraphWrapper graphWrapper = new GraphWrapper();
			graphWrapper.setCityId(config.getCityId());
			graphWrapper.setGraphDirectory(config.getGraphDirectory());
			graphWrapper.setOsmPbfFile(config.getOsmPbfFilePath());
			graphWrapper.setVehicleType(config.getGraphVehicleType());
			graphWrapper.setChEnable(config.isGraphChEnabled());
			graphWrapper.setChWeighting(config.getGraphChWeighting());
			try {
				graphWrapper.loadGraph();
				graphWrappers.add(graphWrapper);
			} catch (Exception e) {
				log.error("Erro while loading graph for {}", config, e);
			}
		}
	}
	
	public DistanceResponse getDistanceResponse(DistanceRequest distanceRequest) {
		if (distanceRequest != null 
				&& isNotEmpty(distanceRequest.getSources())
				&& isNotEmpty(distanceRequest.getDestinations())) {
			List<Row> rows = new ArrayList<Row>(distanceRequest.getSources().size());
			for (Point source : distanceRequest.getSources()) {
				List<Element> elements = new ArrayList<Element>(distanceRequest.getDestinations().size());
				Row row = new Row(elements);
				for (Point destination : distanceRequest.getDestinations()) {
					elements.add(getDistanceElement(source,destination));
				}
				rows.add(row);
			}
			return new DistanceResponse(Status.SUCCESS.getCode(), rows);
		} else {
			return new DistanceResponse(Status.INVALID_REQUEST.getCode());
		}
	}

	public DistanceResponse getDistanceResponse(DistanceTupleRequest distanceTupleRequest) {
		if (distanceTupleRequest != null && distanceTupleRequest.getTuples() != null
				&& distanceTupleRequest.getTuples().size() > 0) {
			List<Tuples> tuples = distanceTupleRequest.getTuples();
			List<Element> elements = new ArrayList<Element>(tuples.size());
			List<Row> rows = new ArrayList<>();
			for (Tuples t : tuples) {
				elements.add(getDistanceElement(t.getPoints().get(0), t.getPoints().get(1)));
			}
			rows.add(new Row(elements));
			return new DistanceResponse(Status.SUCCESS.getCode(), rows);
		} else {
			return new DistanceResponse(Status.INVALID_REQUEST.getCode());
		}
	}

	private Element getDistanceElement(Point source,Point destination){
		Element element;
		GraphWrapper graphWrapper = getGraphWrapper(source);
		if(graphWrapper != null){
			boolean canHandle = graphWrapper.canHandle(source,destination);
			if(canHandle){
				element = graphWrapper.getDistance(source,destination);
			} else {
				element = new Element(Status.NOT_SUPPORTED.getCode());
			}
		} else{
			element = new Element(Status.NOT_SUPPORTED.getCode());
		}
		return element;
	}
	
	private GraphWrapper getGraphWrapper(Point from) {
		for (GraphWrapper graphWrapper : graphWrappers) {
			if (graphWrapper.canHandle(from)) {
				return graphWrapper;
			}
		}
		return null;
	}
	
	private void populateGraphConfig() {
		log.info("Graph Config Json : {}", graphConfigJson);
		if (isNotBlank(graphConfigJson)) {
			Type type = new TypeToken<List<GraphConfig>>() {
					private static final long serialVersionUID = 1L;
				}.getType();
			graphConfigs = new Gson().fromJson(graphConfigJson, type);
		}
	}
}
