package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
public class ActiveService {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Integer id;

	private String service;

	@Column(name = "mydate")
	private LocalDate date;

	@Column(name = "service_time")
	private LocalTime time;

	@Column(name = "formatted_date")
	private String formattedDate;

	@Column(name = "formatted_time")
	private String formattedTime;

	private String leader;

	private String video;
}
