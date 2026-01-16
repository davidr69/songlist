package com.lavacro.songlist.repository;

import java.util.List;

import com.lavacro.songlist.model.SongSetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongSetsRepository extends JpaRepository<SongSetsEntity,Integer> {
	@Query(value = """
		SELECT ROW_NUMBER() OVER(order by mydate) as row_num, cs.mydate, cs.id AS service_id,
			s.description AS service, TO_CHAR(cs.mydate, 'Dy, Mon DD, YYYY') AS formatted_date,
			cs.service_time, TO_CHAR(cs.service_time, 'HH12:MI pm') AS formatted_time, cs.video,
			v."name" AS leader, sng.title, sng.author, sng.marker,
			COALESCE(cd.key_override, sng.note) AS key
		FROM calendar_summary cs
		JOIN services s ON cs.service = s.id
		LEFT JOIN vocalists v ON cs.leader = v.id
		JOIN calendar_details cd ON cs.id = cd.calendar_id
		JOIN songs sng ON cd.song = sng.id
		WHERE s.id = :service_id
			AND (0 = :song_id OR sng.id = :song_id)
			AND (0 = :leader OR cs.leader = :leader)
		ORDER BY mydate DESC, service_time DESC, cd.sort
	""", nativeQuery = true)
	List<SongSetsEntity> getSongSets(
			@Param("service_id") final Integer serviceId,
			@Param("song_id") final Integer songId,
			@Param("leader") final Integer leader
	);
}
