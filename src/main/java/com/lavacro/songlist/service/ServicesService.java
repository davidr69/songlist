package com.lavacro.songlist.service;

import com.lavacro.songlist.model.CalendarSummaryEntity;
import com.lavacro.songlist.model.ServiceEntity;
import com.lavacro.songlist.repository.CalendarSummaryRepository;
import com.lavacro.songlist.repository.ServicesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServicesService {
	private final ServicesRepository servicesRepository;
	private final CalendarSummaryRepository calendarSummaryRepository;

	ServicesService(
		ServicesRepository servicesRepository,
		CalendarSummaryRepository calendarSummaryRepository) {
		this.servicesRepository = servicesRepository;
		this.calendarSummaryRepository = calendarSummaryRepository;
	}

	public ServiceEntity getServiceById(final Integer id) {
		log.info("getServiceById");
		Optional<ServiceEntity> service = servicesRepository.findById(id);
		return service.orElse(null);
	}

	public List<ServiceEntity> getAllServices() {
		Sort sort = Sort.by(Sort.Direction.ASC, "description");
		return servicesRepository.findAll(sort);
	}

	public List<CalendarSummaryEntity> getActiveServices() {
		log.info("getActiveServices");
		return calendarSummaryRepository.getCalendarSummary();
	}

	public CalendarSummaryEntity getActiveServiceById(final Integer id) {
		log.info("getActiveServiceById");
		return calendarSummaryRepository.getOneService(id);
	}

	public List<CalendarSummaryEntity> getSongSets(final Integer service, final Integer songId, final Integer leader) {
		log.info("get song sets");

		return calendarSummaryRepository.getSets(service, songId, leader);
	}
}
