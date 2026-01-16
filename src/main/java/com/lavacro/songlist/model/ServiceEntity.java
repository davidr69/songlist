package com.lavacro.songlist.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "services")
public class ServiceEntity {
	@Id
	private Integer id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "default_time")
	private LocalTime serviceTime;
}
