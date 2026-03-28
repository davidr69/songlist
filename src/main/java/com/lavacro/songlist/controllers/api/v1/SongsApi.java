package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.SongListException;
import com.lavacro.songlist.model.*;
import com.lavacro.songlist.service.LanguagesService;
import com.lavacro.songlist.service.ServicesService;
import com.lavacro.songlist.service.SongsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/v1/songs")
@RestController
@Slf4j
public class SongsApi {
	private final SongsService songsService;
	private final LanguagesService languagesService;
	private final ServicesService servicesService;

	SongsApi(SongsService songsService, LanguagesService languagesService, ServicesService servicesService) {
		this.songsService = songsService;
		this.languagesService = languagesService;
		this.servicesService = servicesService;
	}

	@GetMapping
	public ResponseEntity<List<SongEntity>> allSongs() {
		return new ResponseEntity<>(songsService.getAllSongs(), HttpStatus.OK);
	}

	@GetMapping(value = "/languages")
	public ResponseEntity<List<LanguageEntity>> getLanguages() {
		return new ResponseEntity<>(languagesService.getAllLanguages(), HttpStatus.OK);
	}

	@GetMapping(value = "/service/{service}")
	/*
	 * Returns all songs that have been selected for a service type
	 */
	public List<SongEntity> getSongsForService(@PathVariable("service") final Integer service) {
		return songsService.findSongsForService(service);
	}

	@GetMapping(path = "/sets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CalendarSummaryEntity>> getSongSet(
			@RequestParam(value = "service") Integer service,
			@RequestParam(value = "song") Integer song,
			@RequestParam(value = "leader") Integer leader
	) {

		return new ResponseEntity<>(servicesService.getSongSets(service, song, leader), HttpStatus.OK);
	}

	@PutMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> createSongList(
			@RequestParam(value = "songs") List<Integer> songlist,
			@RequestParam(value = "service") Integer service
	) {
		log.info("update songs: {}, service: {}", songlist, service);
		GenericResponse resp = new GenericResponse();
		songsService.updateSongs(songlist, service);
		resp.setSuccess(true);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NewListResponse> createSongList(
			@RequestParam(value = "songs") List<Integer> songlist,
			@RequestParam(value = "serviceType") Integer serviceType,
			@RequestParam(value = "month") Integer month,
			@RequestParam(value = "day") Integer day,
			@RequestParam(value = "year") Integer year,
			@RequestParam(value = "hour") Integer hour,
			@RequestParam(value = "minute") Integer minute
	) {
		log.info("add songs: {}, service: {}", songlist, serviceType);
		NewListResponse resp = new NewListResponse();
		try {
			Integer num = songsService.addSongs(songlist, serviceType, month, day, year, hour, minute);
			resp.setNewListId(num);
			resp.setSuccess(true);
		} catch(SongListException e) {
			resp.setSuccess(false);
			resp.setMessage(e.getMessage());
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
