package com.swiggy.distance.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
@ToString
public class GraphConfig {

	private int cityId;
	private String osmPbfFilePath;
	private String graphDirectory;
	private String graphVehicleType;
	private String graphChWeighting;
	private boolean graphChEnabled;
	
	public GraphConfig(int cityId, String osmPbfFilePath, String graphDirectory) {
		this.cityId = cityId;
		this.osmPbfFilePath = osmPbfFilePath;
		this.graphDirectory = graphDirectory;
	}
	
	public static void main(String[] args) {
		List<GraphConfig> configs = new ArrayList<GraphConfig>();
		configs.add(new GraphConfig(1, "/Users/bengaluru.pbf", "/Users/bengaluru-gh/", "car,motorcycle", "shortest", true));
		System.out.println(new Gson().toJson(configs));
	}
}
