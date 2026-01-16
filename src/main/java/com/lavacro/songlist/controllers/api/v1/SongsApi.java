package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.SongListException;
import com.lavacro.songlist.model.*;
import com.lavacro.songlist.service.LanguagesService;
import com.lavacro.songlist.service.ReportsService;
import com.lavacro.songlist.service.SongsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/v1/songs")
@RestController
public class SongsApi {
	private final static Logger LOGGER = LoggerFactory.getLogger(SongsApi.class);

	private final SongsService songsService;
	private final ReportsService reportsService;
	private final LanguagesService languagesService;

	SongsApi(SongsService songsService, ReportsService reportsService, LanguagesService languagesService) {
		this.songsService = songsService;
		this.reportsService = reportsService;
		this.languagesService = languagesService;
	}

	@GetMapping
	public ResponseEntity<List<Song>> allSongs() {
		return new ResponseEntity<>(songsService.getAllSongs(), null, HttpStatus.OK);
	}

	@GetMapping(value = "/languages")
	public ResponseEntity<List<LanguageEntity>> getLanguages() {
		return new ResponseEntity<>(languagesService.getAllLanguages(), null, HttpStatus.OK);
	}

	@GetMapping(value = "/service/{service}")
	/*
	 * Returns all songs that have been selected for a service type
	 */
	public List<Song> getSongsForService(@PathVariable("service") final Integer service) {
		return songsService.findSongsForService(service);
	}

	@GetMapping(path = "/selected/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SetLineItem>> selectedSongs(
			@PathVariable(name = "id") Integer id
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Expires", "0");
		headers.add("Pragma", "no-cache");
		return new ResponseEntity<>(songsService.selectedSongs(id), headers, HttpStatus.OK);
	}

	@GetMapping(path = "/sets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SongSet>> getSongSet(
			@RequestParam(value = "service") Integer service,
			@RequestParam(value = "song") Integer song,
			@RequestParam(value = "leader") Integer leader
	) {

		return new ResponseEntity<>(reportsService.getSongSets(service, song, leader), null, HttpStatus.OK);
	}

	@PutMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> createSongList(
			@RequestParam(value = "songs") List<Integer> songlist,
			@RequestParam(value = "service") Integer service
	) {
		LOGGER.info("update songs: {}, service: {}", songlist, service);
		GenericResponse resp = new GenericResponse();
		songsService.updateSongs(songlist, service);
		resp.setSuccess(true);
		return new ResponseEntity<>(resp, null, HttpStatus.OK);
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
		LOGGER.info("add songs: {}, service: {}", songlist, serviceType);
		NewListResponse resp = new NewListResponse();
		try {
			Integer num = songsService.addSongs(songlist, serviceType, month, day, year, hour, minute);
			resp.setNewListId(num);
			resp.setSuccess(true);
		} catch(SongListException e) {
			resp.setSuccess(false);
			resp.setMessage(e.getMessage());
		}
		return new ResponseEntity<>(resp, null, HttpStatus.OK);
	}

}
