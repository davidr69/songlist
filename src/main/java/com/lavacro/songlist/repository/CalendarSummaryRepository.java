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
		JOIN FETCH cs.service
		LEFT JOIN FETCH cs.details d
		LEFT JOIN FETCH d.leaderEntity
		LEFT JOIN FETCH d.songEntity
		ORDER BY cs.mydate DESC, cs.service.description
	""")
	List<CalendarSummaryEntity> getCalendarSummary();

	@Query("""
		SELECT cs
		FROM CalendarSummaryEntity cs
		JOIN FETCH cs.service
		LEFT JOIN FETCH cs.details d
		LEFT JOIN FETCH d.songEntity
		LEFT JOIN FETCH d.leaderEntity
		WHERE cs.id = :id
		ORDER BY d.id.sort
	""")
	CalendarSummaryEntity getOneService(@Param(value = "id") Integer id);

	@Query("""
		SELECT DISTINCT cs
		FROM CalendarSummaryEntity cs
		JOIN FETCH cs.service
		LEFT JOIN FETCH cs.details d
		LEFT JOIN FETCH d.leaderEntity
		LEFT JOIN FETCH d.songEntity
		WHERE
			cs.service.id = :service
			AND (d.songEntity.id = :song_id OR :song_id = 0)
			AND (cs.leaderEntity.id = :leader_id OR :leader_id = 0)
		ORDER BY cs.mydate DESC, cs.service.description, d.id.sort
	""")
	List<CalendarSummaryEntity> getSets(
		@Param(value = "service") Integer service,
		@Param(value = "song_id") Integer songId,
		@Param(value = "leader_id") Integer leader
	);
}
