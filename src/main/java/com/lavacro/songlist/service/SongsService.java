package com.lavacro.songlist.service;

import com.lavacro.songlist.SongListException;
import com.lavacro.songlist.model.*;
import com.lavacro.songlist.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SongsService.class);

	private final SongsRepository songsRepository;
	private final SetsRepository setsRepository;
	private final CalendarDetailsRepository calendarDetailsRepository;
	private final CalendarSummaryRepository calendarSummaryRepository;
	private final ServicesRepository servicesRepository;

	public SongsService(SongsRepository songsRepository, SetsRepository setsRepository,
				CalendarDetailsRepository calendarDetailsRepository,
				CalendarSummaryRepository calendarSummaryRepository,
				ServicesRepository servicesRepository) {
		this.songsRepository = songsRepository;
		this.setsRepository = setsRepository;
		this.calendarDetailsRepository = calendarDetailsRepository;
		this.calendarSummaryRepository = calendarSummaryRepository;
		this.servicesRepository = servicesRepository;
	}

	public List<SetLineItem> selectedSongs(final Integer id) {
		LOGGER.info("selected songs");
		return setsRepository.getSetList(id);
	}

	public List<SongEntity> getAllSongs() {
		LOGGER.info("Get all songs");
		return songsRepository.findAllSortByTitle();
	}

	public Integer addSongs(final List<Integer> songs, final Integer serviceType, final Integer month,
				final Integer day, final Integer year, final Integer hour, final Integer min) throws SongListException {
		LOGGER.info("add songs");
		// am I trying to create a list for a service which already exists?
		LocalDate ld = LocalDate.of(year, month, day);

		ServiceEntity serviceEntity = servicesRepository.getReferenceById(serviceType);

		CalendarSummaryEntity calendarSummaryEntity = new CalendarSummaryEntity();
		calendarSummaryEntity.setService(serviceEntity);
		calendarSummaryEntity.setMydate(ld.atStartOfDay());
		calendarSummaryEntity.setServiceTime(LocalTime.of(hour, min));
		Example<CalendarSummaryEntity> example = Example.of(calendarSummaryEntity);
		Optional<CalendarSummaryEntity> resp = calendarSummaryRepository.findOne(example);

		Integer id;
		if(resp.isEmpty()) {
			calendarSummaryRepository.save(calendarSummaryEntity);

			id =  calendarSummaryEntity.getId();
			insertData(songs, id);
		} else {
			LOGGER.error("Service already exists");
			throw new SongListException("Service already exists");
		}
		return id;
	}

	public void updateSongs(final List<Integer> songs, final Integer service) {
		LOGGER.info("update songs");
		songsRepository.deleteFromCalendar(service);
		insertData(songs, service);
	}

	private void insertData(final List<Integer> songs, final Integer serviceId) {
		int sort = 1;

		CalendarSummaryEntity calendarSummaryEntity = calendarSummaryRepository.getReferenceById(serviceId);
		List<CalendarDetailsEntity> batch = new ArrayList<>();

		for(Integer songId: songs) {
			CalendarDetailsId id = new CalendarDetailsId().calendarId(serviceId).sort(sort++);
			CalendarDetailsEntity entity = new CalendarDetailsEntity();
			entity.setCalendar(calendarSummaryEntity);
			entity.setId(id);

			SongEntity songEntity = songsRepository.getReferenceById(songId);
			entity.setSongEntity(songEntity);
			batch.add(entity);
		}

		calendarDetailsRepository.saveAll(batch);
	}

	public List<SongEntity> findSongsForService(final Integer service) {
		return songsRepository.findSongsForService(service);
	}
}
