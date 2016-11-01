package com.swiggy.distance.responses;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@JsonSerialize
@NoArgsConstructor
public class DistanceResponse {

	private int status;
	private List<Row> rows;
	
	public DistanceResponse(int status) {
		this.status = status;
	}
}
