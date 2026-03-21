package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.CalendarSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarSummaryRepository extends JpaRepository<CalendarSummaryEntity, Integer> {
	@Query("""
		SELECT cs
		FROM CalendarSummaryEntity cs
		JOIN FETCH cs.services
	""")
	List<CalendarSummaryEntity> getCalendarSummary();
}
