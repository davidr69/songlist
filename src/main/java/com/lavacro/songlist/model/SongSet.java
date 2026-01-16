package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SongSet {
	private ActiveService service;
	private List<Song> songs;
}
