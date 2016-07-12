package com.thinktankcrew.social.hackathon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
		"status",
		"responses",
		"sessionid"
})
public class MitsukuResponse {

	@JsonProperty("status")
	private String status;
	@JsonProperty("responses")
	private List<String> responses = new ArrayList<String>();
	@JsonProperty("sessionid")
	private Integer sessionid;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 *
	 * @param status
	 * The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 *
	 * @return
	 * The responses
	 */
	@JsonProperty("responses")
	public List<String> getResponses() {
		return responses;
	}

	/**
	 *
	 * @param responses
	 * The responses
	 */
	@JsonProperty("responses")
	public void setResponses(List<String> responses) {
		this.responses = responses;
	}

	/**
	 *
	 * @return
	 * The sessionid
	 */
	@JsonProperty("sessionid")
	public Integer getSessionid() {
		return sessionid;
	}

	/**
	 *
	 * @param sessionid
	 * The sessionid
	 */
	@JsonProperty("sessionid")
	public void setSessionid(Integer sessionid) {
		this.sessionid = sessionid;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
