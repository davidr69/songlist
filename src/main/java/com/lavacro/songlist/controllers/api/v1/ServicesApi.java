package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.model.ActiveService;
import com.lavacro.songlist.model.ServiceEntity;
import com.lavacro.songlist.service.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/api/v1/services")
@RestController
public class ServicesApi {
	private final ServicesService servicesService;

	ServicesApi(ServicesService servicesService) {
		this.servicesService = servicesService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceEntity>> services() {
		return new ResponseEntity<>(servicesService.getAllServices(), null, HttpStatus.OK);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<ServiceEntity> service(@PathVariable(name = "id") final Integer id) {
		return new ResponseEntity<>(servicesService.getServiceById(id), null, HttpStatus.OK);
	}

	@GetMapping(path = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActiveService>> activeServices() {
		return new ResponseEntity<>(servicesService.getActiveServices(), null, HttpStatus.OK);
	}

	@GetMapping(path = "/active/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ActiveService> activeService(@PathVariable("serviceId") Integer serviceId) {
		return new ResponseEntity<>(servicesService.getActiveServiceById(serviceId), null, HttpStatus.OK);
	}

}
