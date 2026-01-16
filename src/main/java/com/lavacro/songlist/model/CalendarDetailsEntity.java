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

	private Integer song;

	private Integer leader;

	private String notes;

	@Column(name = "key_override")
	private String keyOverride;
}
