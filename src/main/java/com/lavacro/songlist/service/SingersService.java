package com.lavacro.songlist.service;

import com.lavacro.songlist.model.Leader;
import com.lavacro.songlist.model.Song;
import com.lavacro.songlist.repository.LeadersRepository;
import com.lavacro.songlist.repository.SongsRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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

	public List<Song> singerSonglist(final Integer singer) {
		log.info("singerSonglist");
		return songsRepository.findSongsForSinger(singer);
	}

	public List<Leader> getLeaders() {
		log.info("getLeaders");
		return leadersRepository.findAll();
	}

	public List<Leader> getLeadersForService(final Integer service) {
		log.info("getLeadersForService");
		return leadersRepository.getLeadersForService(service);
	}
}
