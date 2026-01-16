package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.CalendarDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarDetailsRepository extends JpaRepository<CalendarDetailsEntity, Integer> {
}
