package com.lavacro.songlist.service;

import com.lavacro.songlist.model.LeaderEntity;
import com.lavacro.songlist.model.SongEntity;
import com.lavacro.songlist.repository.LeadersRepository;
import com.lavacro.songlist.repository.SongsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SingersService {
	private final LeadersRepository leadersRepository;
	private final SongsRepository songsRepository;

	public SingersService(LeadersRepository leadersRepository, SongsRepository songsRepository) {
		this.leadersRepository = leadersRepository;
		this.songsRepository = songsRepository;
	}

	public List<SongEntity> singerSonglist(final Integer singer) {
		log.info("singerSonglist");
		return songsRepository.findSongsForSinger(singer);
	}

	public List<LeaderEntity> getLeaders() {
		log.info("getLeaders");
		return leadersRepository.findAll();
	}

	public List<LeaderEntity> getLeadersForService(final Integer service) {
		log.info("getLeadersForService");
		return leadersRepository.getLeadersForService(service);
	}
}
