package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadersRepository extends JpaRepository<Leader, Integer> {
	@Query(value = """
		SELECT DISTINCT v.id, v.name
		FROM vocalists v
		JOIN calendar_summary cs ON cs.leader = v.id
		WHERE cs.service = :service
		ORDER BY v.name;
	""", nativeQuery = true)
	List<Leader> getLeadersForService(@Param(value = "service") final Integer service);
}
