package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
public class SetLineItem {
	@Id
	@Column(name = "row_num")
	private Integer rowNum;

	private Integer id;

	private String title;

	private String note;

	@Column(name = "bpm")
	private Integer tempo;

	private String author;

	private Boolean marker;

	private String leader;

	private String memo;

	private String aka;
}
