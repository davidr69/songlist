package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.service.SingersService;
import com.lavacro.songlist.model.Leader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/api/v1/leaders")
@RestController
public class LeadersApi {
	private final SingersService singersService;

	public LeadersApi(SingersService singersService) {
		this.singersService = singersService;
	}

	@Operation(summary = "Gets all worship leaders for a specific church/service")
	@GetMapping
	public List<Leader> getLeadersForService(
			@Parameter(description = "An Integer representing the church/service ID")
			@RequestParam(value = "service") final Integer service) {
		return singersService.getLeadersForService(service);
	}
}
