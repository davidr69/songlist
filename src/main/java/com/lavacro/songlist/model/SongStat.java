package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
public class SongStat {
	@Id
	private Integer id;

	@Column(name = "title")
	private String songname;

	private String author;

	private Integer qty;
}
