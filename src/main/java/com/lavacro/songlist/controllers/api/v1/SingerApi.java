package com.lavacro.songlist.controllers.api.v1;

import com.lavacro.songlist.model.*;

import com.lavacro.songlist.service.SingersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/singers")
public class SingerApi {
	private final SingersService singersService;

	public SingerApi(SingersService singersService) {
		this.singersService = singersService;
	}

	@GetMapping(path = "/songlist/{singer}")
	public ResponseEntity<List<Song>> singerSongList(@PathVariable(name = "singer") final Integer singer) {
		return new ResponseEntity<>(singersService.singerSonglist(singer), null, HttpStatus.OK);
	}

	@GetMapping(path = "/list")
	public ResponseEntity<List<Leader>> getLeaders() {
		return new ResponseEntity<>(singersService.getLeaders(), null, HttpStatus.OK);
	}
}
