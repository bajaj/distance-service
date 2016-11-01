package com.swiggy.jackson.databind;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.guava.GuavaModule;
//import com.fasterxml.jackson.datatype.joda.JodaModule;

public class SwiggyObjectMapper extends ObjectMapper {
	
	public SwiggyObjectMapper() {
//		this.registerModule(new JodaModule());
//		this.registerModule(new GuavaModule());
		this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		this.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
	}

}
