package com.lavacro.songlist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(fluent = true)
@Embeddable
@EqualsAndHashCode
public class CalendarDetailsId implements Serializable {
	@Column(name = "calendar_id")
	private Integer calendarId;

	private Integer sort;
}
