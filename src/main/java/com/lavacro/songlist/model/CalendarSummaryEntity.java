package com.lavacro.songlist.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "calendar_summary")
public class CalendarSummaryEntity {
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter dowFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cal_sum_generator")
	@SequenceGenerator(name = "cal_sum_generator", sequenceName = "cal_summ_seq", allocationSize = 1)
	private Integer id;

	private LocalDateTime mydate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service")
	@JsonProperty("service")
	private ServiceEntity service;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leader")
	@JsonProperty("leader")
	private LeaderEntity leaderEntity;

	private String video;

	private Boolean duplicate;

	@Column(name = "service_time")
	private LocalTime serviceTime;

	@JsonManagedReference
	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CalendarDetailsEntity> details = new ArrayList<>();

	@Transient
	public String getFormattedDate() {
		return mydate == null ? "" : mydate.format(dateFormatter);
	}

	@Transient
	public String getFormattedDateTime() {
		return mydate == null || service.getServiceTime() == null ? "" :
			mydate.format(dowFormatter) + " " + service.getServiceTime().format(timeFormatter);
	}
}
