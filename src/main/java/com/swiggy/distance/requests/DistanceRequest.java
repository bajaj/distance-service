package com.swiggy.distance.requests;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;

@Getter
@AllArgsConstructor
@ToString
@JsonSerialize
@NoArgsConstructor
public class DistanceRequest {

	private List<Point> sources;
	private List<Point> destinations;
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		DistanceRequest request = new DistanceRequest(Arrays.asList(new Point(1.0, 2.0)),
				Arrays.asList(new Point(1.0, 2.0), new Point(4.0, 6.0)));
		System.out.println(gson.toJson(request));
	}
}
