package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.CalendarSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarSummaryRepository extends JpaRepository<CalendarSummaryEntity, Integer> {
}
