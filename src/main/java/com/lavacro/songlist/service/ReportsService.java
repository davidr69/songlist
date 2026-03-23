package com.lavacro.songlist.service;

import com.lavacro.songlist.model.*;
import com.lavacro.songlist.repository.SongStatsRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportsService {

	private final SongStatsRepository songStatsRepository;

	public ReportsService(
			SongStatsRepository songStatsRepository
	) {
		this.songStatsRepository = songStatsRepository;
	}

	public SongStats chartTopppers(final Integer service) {
		log.info("Song list for service {}", service);
		SongStats stats = new SongStats();
		stats.setSongStat(songStatsRepository.findForService(service));
		return stats;
	}
}
