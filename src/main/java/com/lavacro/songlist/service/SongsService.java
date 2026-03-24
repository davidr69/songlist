package com.lavacro.songlist.service;

import com.lavacro.songlist.SongListException;
import com.lavacro.songlist.model.*;
import com.lavacro.songlist.repository.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SongsService {
	private final SongsRepository songsRepository;
	private final CalendarDetailsRepository calendarDetailsRepository;
	private final CalendarSummaryRepository calendarSummaryRepository;
	private final ServicesRepository servicesRepository;

	public SongsService(SongsRepository songsRepository,
				CalendarDetailsRepository calendarDetailsRepository,
				CalendarSummaryRepository calendarSummaryRepository,
				ServicesRepository servicesRepository) {
		this.songsRepository = songsRepository;
		this.calendarDetailsRepository = calendarDetailsRepository;
		this.calendarSummaryRepository = calendarSummaryRepository;
		this.servicesRepository = servicesRepository;
	}

	public List<SongEntity> getAllSongs() {
		log.info("Get all songs");
		return songsRepository.findAllSortByTitle();
	}

	public Integer addSongs(final List<Integer> songs, final Integer serviceType, final Integer month,
				final Integer day, final Integer year, final Integer hour, final Integer min) throws SongListException {
		log.info("add songs");
		// am I trying to create a list for a service which already exists?
		LocalDate ld = LocalDate.of(year, month, day);

		ServiceEntity serviceEntity = servicesRepository.findById(serviceType).orElse(null);

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
			log.error("Service already exists");
			throw new SongListException("Service already exists");
		}
		return id;
	}

	public void updateSongs(final List<Integer> songs, final Integer service) {
		log.info("update songs");
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
