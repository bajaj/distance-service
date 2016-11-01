package com.swiggy.distance.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum City {

	BANGALORE(1), 
	GURGAON(2), 
	HYDERABAD(3),
	DELHI(4),
	MUMBAI(5),
	PUNE(6),
	KOLKATA(7),
	CHENNAI(8);
	private final int id;
	
	public static City findByValue(int cityId) {
		for (City city : values()) {
			if (city.id == cityId) {
				return city;
			}
		}
		return null;
	}
}
