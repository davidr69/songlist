package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SongsRepository extends JpaRepository<Song, Integer> {
	@Query(value = """
			SELECT id, title, bpm, note, author, praise, marker, aka, language
			FROM songs
			ORDER BY marker, spanish_sort(title)
	""", nativeQuery = true)
	List<Song> findAllSortByTitle();

	@Query(value = "DELETE FROM calendar_details WHERE calendar_id = :calendar_id", nativeQuery = true)
	@Modifying
	@Transactional
	void deleteFromCalendar(@Param("calendar_id") final Integer calendarId);

	@Query(value = """
			SELECT s.id, s.title, s.bpm, COALESCE(ko.note, s.note) AS note, s.author, s.praise, s.marker,
				aka, language
			FROM songs s
			LEFT JOIN key_override ko ON s.id = ko.song
			WHERE ko.singer = :singer OR ko.singer IS NULL
			ORDER BY marker, spanish_sort(title)
	""", nativeQuery = true)
	List<Song> findSongsForSinger(@Param("singer") final Integer singer);

	@Query(value = """
		SELECT * FROM (
			SELECT DISTINCT s.id, s.title, s.bpm, s.note, s.author, s.praise, s.marker, s.aka, s.language
			FROM songs s
			JOIN calendar_details cd ON cd.song = s.id
			JOIN calendar_summary cs ON cs.id = cd.calendar_id
			WHERE cs.service = :service
		) AS foo
		ORDER BY marker, spanish_sort(title)
	""", nativeQuery = true)
	List<Song> findSongsForService(@Param("service") final Integer service);
}
