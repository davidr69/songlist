package com.lavacro.songlist.model;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Setter
@Getter
@Table(name = "language")
public class LanguageEntity {
	@Id
	private Integer id;

	@Column(name = "language_name")
	private String language;
}
