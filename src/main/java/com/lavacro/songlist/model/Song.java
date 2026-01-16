package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "songs")
public class Song {
	@Id
	private Integer id;

	private String title;

	@Column(name = "bpm")
	private Integer tempo;

	@Column(name = "note")
	private String key;

	private String author;

	private Boolean praise;

	private Boolean marker;

	@Column(name = "aka")
	private String alias;

	private Integer language;
}
