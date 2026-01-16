package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.SongStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongStatsRepository extends JpaRepository<SongStat, Integer> {
	@Query(value = """
			WITH x AS (
				SELECT DISTINCT title, author, title || ' (' || COALESCE(author, '') || ')' AS songname, mydate, s.id
				FROM calendar_summary cs
				JOIN calendar_details cd ON cs.id = cd.calendar_id
				JOIN songs s ON cd.song = s.id
				WHERE cs.service = :service_id AND song > 0 AND duplicate IS NULL OR duplicate = false
			)
			SELECT id, title, author, COUNT(songname) AS qty
			FROM x
			GROUP BY id, title, author
			ORDER BY qty DESC, title
	""", nativeQuery = true
	)
	List<SongStat> findForService(@Param("service_id") final Integer serviceId);
}
