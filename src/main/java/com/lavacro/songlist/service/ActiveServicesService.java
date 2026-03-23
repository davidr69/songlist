package com.lavacro.songlist.service;

import com.lavacro.songlist.model.ActiveServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Rather than use the JPA model and repository, which returns not only this list but all the songs for each service,
 * this is a much more streamlined and efficient approach.
 */
@Service
@Slf4j
public class ActiveServicesService {
	private final DataSource dataSource;

	public ActiveServicesService(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<ActiveServiceDTO> getActiveServices() {
		String sql = """
			SELECT TO_CHAR(cs.mydate, 'Dy, Mon DD, YYYY') AS formatted_date, cs.id AS service_id, s.description AS service, cs.service_time
			FROM calendar_summary cs
			JOIN services s ON cs.service = s.id
			ORDER BY mydate DESC, service
		""";

		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()
		) {
			List<ActiveServiceDTO> activeServices = new java.util.ArrayList<>();
			while (rs.next()) {
				ActiveServiceDTO dto = new ActiveServiceDTO(
					rs.getInt("service_id"),
					rs.getString("service"),
					rs.getString("formatted_date"),
					rs.getString("service_time")
				);
				activeServices.add(dto);
			}
			return activeServices;
		} catch (SQLException e) {
			log.error("Error getting active services", e);
		}

		return Collections.emptyList();
	}
}
