package com.lavacro.songlist.service;

import com.lavacro.songlist.model.ActiveService;
import com.lavacro.songlist.model.ServiceEntity;
import com.lavacro.songlist.repository.ActiveServicesRepository;
import com.lavacro.songlist.repository.ServicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesService.class);

	private final ServicesRepository servicesRepository;
	private final ActiveServicesRepository activeServicesRepository;

	ServicesService(ServicesRepository servicesRepository, ActiveServicesRepository activeServicesRepository) {
		this.servicesRepository = servicesRepository;
		this.activeServicesRepository = activeServicesRepository;
	}

	public ServiceEntity getServiceById(final Integer id) {
		LOGGER.info("getServiceById");
		Optional<ServiceEntity> service = servicesRepository.findById(id);
		return service.orElse(null);
	}

	public List<ServiceEntity> getAllServices() {
		Sort sort = Sort.by(Sort.Direction.ASC, "description");
		return servicesRepository.findAll(sort);
	}

	public List<ActiveService> getActiveServices() {
		return activeServicesRepository.getActiveServices();
	}

	public ActiveService getActiveServiceById(final Integer id) {
		return activeServicesRepository.getOneService(id);
	}
}
