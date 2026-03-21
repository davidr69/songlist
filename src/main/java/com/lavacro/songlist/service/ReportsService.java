package com.lavacro.songlist.service;

import com.lavacro.songlist.model.*;
import com.lavacro.songlist.repository.SongSetsRepository;
import com.lavacro.songlist.repository.SongStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportsService {
	private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

	private final SongStatsRepository songStatsRepository;
	private final SongSetsRepository songSetsRepository;

	public ReportsService(SongStatsRepository songStatsRepository, SongSetsRepository songSetsRepository) {
		this.songStatsRepository = songStatsRepository;
		this.songSetsRepository = songSetsRepository;
	}

	public SongStats chartTopppers(final Integer service) {
		logger.info("Song list for service {}", service);
		SongStats stats = new SongStats();
		stats.setSongStat(songStatsRepository.findForService(service));
		return stats;
	}

	public List<SongSet> getSongSets(final Integer service, final Integer songId, final Integer leader) {
		logger.info("get song sets");

		List<SongSetsEntity> songSetsEntities = songSetsRepository.getSongSets(service, songId, leader);

		List<SongSet> rv = new ArrayList<>();
		SongSet songSet = null;
		List<SongEntity> songEntities = null;

		for(SongSetsEntity entity: songSetsEntities) {
			Integer serviceId = entity.getServiceId();
			if (songSet == null || !songSet.getService().getId().equals(serviceId)) {
				songSet = new SongSet();
				ActiveService actSvc = new ActiveService();
				actSvc.setId(serviceId);
				actSvc.setService(entity.getServiceName());
				actSvc.setLeader(entity.getLeader());
				actSvc.setDate(entity.getServiceDate());
				actSvc.setFormattedDate(entity.getFormattedDate());
				actSvc.setVideo(entity.getVideo());
				actSvc.setTime(entity.getServiceTime());
				actSvc.setFormattedTime(entity.getFormattedTime());
				songSet.setService(actSvc);
				songEntities = new ArrayList<>();
				songSet.setSongs(songEntities);
				rv.add(songSet);
			}

			SongEntity songEntity = new SongEntity();
			songEntity.setMarker(entity.getMarker());
			songEntity.setTitle(entity.getTitle());
			songEntity.setKey(entity.getKey());
			songEntity.setAuthor(entity.getAuthor());
			songEntities.add(songEntity);
		}
		return rv;
	}
}
