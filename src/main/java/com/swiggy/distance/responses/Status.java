package com.swiggy.distance.responses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Status {

	SUCCESS(0), ERROR(1), NOT_SUPPORTED(3), INVALID_REQUEST(4);
	private int code;
}
