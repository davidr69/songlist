package com.lavacro.songlist.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "vocalists")
@Entity
@Getter
@Setter
public class Leader {
    @Id
    private Integer id;
    private String name;
}
