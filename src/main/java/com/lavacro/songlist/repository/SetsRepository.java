package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.SetLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetsRepository extends JpaRepository<SetLineItem, Integer> {
	@Query(value = """
			SELECT ROW_NUMBER() OVER(ORDER by sort) as row_num, s.id, s.title,
				COALESCE(cd.key_override, s.note) AS note, s.bpm, s.author,
				s.marker, cd.notes AS memo, v.name AS leader, s.aka
			FROM calendar_summary cs
			JOIN calendar_details cd ON cs.id = cd.calendar_id
			JOIN songs s ON cd.song = s.id
			LEFT JOIN vocalists v ON cd.leader = v.id
			WHERE cs.id = :calendarId
			ORDER BY sort
		""", nativeQuery = true
	)
	List<SetLineItem> getSetList(@Param("calendarId") final Integer calendarId);
}
