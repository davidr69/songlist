package com.lavacro.songlist.model;

import java.util.ArrayList;
import java.util.List;

public class SongStats extends GenericResponse {
	private List<SongStat> songStat;

	public List<SongStat> getSongStat() {
		if(songStat == null) {
			songStat = new ArrayList<SongStat>();
		}
		return songStat;
	}
	public void setSongStat(List<SongStat> songStat) { this.songStat = songStat; }
}
