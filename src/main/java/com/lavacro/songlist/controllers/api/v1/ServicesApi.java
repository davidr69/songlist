package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.model.ActiveServiceDTO;
import com.lavacro.songlist.model.CalendarSummaryEntity;
import com.lavacro.songlist.model.ServiceEntity;
import com.lavacro.songlist.service.ActiveServicesService;
import com.lavacro.songlist.service.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Services API", description = "Endpoints for retrieving service types and active services")
@RequestMapping(value = "/api/v1/services")
@RestController
public class ServicesApi {
	private final ServicesService servicesService;
	private final ActiveServicesService activeServicesService;

	ServicesApi(ServicesService servicesService, ActiveServicesService activeServicesService) {
		this.servicesService = servicesService;
		this.activeServicesService = activeServicesService;
	}

	@Operation(summary = "Gets all service types")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceEntity>> services() {
		return new ResponseEntity<>(servicesService.getAllServices(), HttpStatus.OK);
	}

	@Operation(summary = "Gets a service type by id")
	@GetMapping(path = "/{id}")
	public ResponseEntity<ServiceEntity> service(
		@Parameter(description = "The database id of a service type") @PathVariable final Integer id
	) {
		return new ResponseEntity<>(servicesService.getServiceById(id), HttpStatus.OK);
	}

	@Operation(summary = "Gets all service instances")
	@GetMapping(path = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActiveServiceDTO>> activeServices() {
		return new ResponseEntity<>(activeServicesService.getActiveServices(), HttpStatus.OK);
	}

	@Operation(summary = "Gets one service instance with song details")
	@GetMapping(path = "/active/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CalendarSummaryEntity> activeService(@PathVariable Integer serviceId) {
		return new ResponseEntity<>(servicesService.getActiveServiceById(serviceId), HttpStatus.OK);
	}
}
