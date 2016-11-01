package com.swiggy.distance.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Element {

	private int status;
	private double distance;
	
	public Element(int status) {
		this.status = status;
	}
}
