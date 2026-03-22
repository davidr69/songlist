package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.CalendarSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarSummaryRepository extends JpaRepository<CalendarSummaryEntity, Integer> {
	@Query("""
		SELECT DISTINCT cs
		FROM CalendarSummaryEntity cs
		JOIN FETCH cs.services
		LEFT JOIN FETCH cs.details d
		LEFT JOIN FETCH d.songEntity
		LEFT JOIN FETCH d.leaderEntity
		ORDER BY cs.mydate DESC, cs.services.description
	""")
	List<CalendarSummaryEntity> getCalendarSummary();

	@Query("""
		SELECT cs
		FROM CalendarSummaryEntity cs
		JOIN FETCH cs.services
		LEFT JOIN FETCH cs.details d
		LEFT JOIN FETCH d.songEntity
		LEFT JOIN FETCH d.leaderEntity
		WHERE cs.id = :id
	""")
	CalendarSummaryEntity getOneService(@Param(value = "id") Integer id);
}
