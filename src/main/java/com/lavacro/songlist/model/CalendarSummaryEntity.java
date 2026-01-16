package com.lavacro.songlist.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "calendar_summary")
public class CalendarSummaryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cal_sum_generator")
	@SequenceGenerator(name = "cal_sum_generator", sequenceName = "cal_summ_seq", allocationSize = 1)
	private Integer id;

	private LocalDateTime mydate;

	private Integer service;

	private Integer leader;

	private String video;

	private Boolean duplicate;

	@Column(name = "service_time")
	private LocalTime serviceTime;
}
