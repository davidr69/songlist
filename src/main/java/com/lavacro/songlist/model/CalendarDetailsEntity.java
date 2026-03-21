package com.lavacro.songlist.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="calendar_details")
public class CalendarDetailsEntity {
	@EmbeddedId
	private CalendarDetailsId id;

	@ManyToOne
	@MapsId("calendarId")
	@JoinColumn(name = "calendar_id")
	private CalendarSummaryEntity calendar;

	@ManyToOne
	@JoinColumn(name = "song")
	private SongEntity songEntity;

	@ManyToOne
	@JoinColumn(name = "leader")
	private LeaderEntity leaderEntity;

	private String notes;

	@Column(name = "key_override")
	private String keyOverride;
}
