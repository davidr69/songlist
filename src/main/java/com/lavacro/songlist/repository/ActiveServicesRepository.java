package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.ActiveService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveServicesRepository extends JpaRepository<ActiveService, Integer> {
	@Query(value = """
		SELECT cs.mydate, cs.id AS service_id, s.description AS service, NULL AS formatted_date,
			NULL AS formatted_time, NULL AS leader, service_time, NULL AS video
		FROM calendar_summary cs
		JOIN services s ON cs.service = s.id
		ORDER BY mydate DESC, service
	""", nativeQuery = true)
	List<ActiveService> getActiveServices();

	@Query(value = """
		SELECT cs.mydate, cs.id AS service_id, s.description AS service,
			TO_CHAR(cs.mydate, 'Dy, Mon DD, YYYY') AS formatted_date, cs.service_time,
			cs.video, v."name" AS leader, NULL as formatted_time
		FROM calendar_summary cs
		JOIN services s ON cs.service = s.id
		LEFT JOIN vocalists v ON cs.leader = v.id
		WHERE cs.id = :id
	""", nativeQuery = true)
	ActiveService getOneService(@Param(value = "id") Integer id);
}
