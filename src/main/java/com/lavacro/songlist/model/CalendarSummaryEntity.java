package com.lavacro.songlist.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service")
	private ServiceEntity services;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leader")
	private LeaderEntity leaderEntity;

	private String video;

	private Boolean duplicate;

	@Column(name = "service_time")
	private LocalTime serviceTime;

	@JsonManagedReference
	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<CalendarDetailsEntity> details = new ArrayList<>();
}
