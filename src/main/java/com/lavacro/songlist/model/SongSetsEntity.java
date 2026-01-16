package com.lavacro.songlist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class SongSetsEntity {
	@Id
	@Column(name = "row_num")
	private Integer rowNum;

	@Column(name = "mydate")
	private LocalDate serviceDate;

	@Column(name = "service_time")
	private LocalTime serviceTime;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "service")
	private String serviceName;

	@Column(name = "formatted_date")
	private String formattedDate;

	@Column(name = "formatted_time")
	private String formattedTime;

	private String leader;

	private String title;

	private String author;

	private Boolean marker;

	private String video;

	private String key;
}
