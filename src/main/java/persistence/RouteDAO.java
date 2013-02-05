package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import entity.Coordenada;
import entity.Route;

@RequestScoped
@PersistenceController
public class RouteDAO {

	@Inject
	private DataSource dataSource;

	@Transactional
	public void insert(Route rota) {
		Connection connection;
		try {
			connection = dataSource.getConnection();

			StringBuffer sql = new StringBuffer();
			sql.append("insert into routes (description, username, geom) ");
			sql.append("values (?, ?, geomfromtext(?))");

			PreparedStatement pstmt = connection.prepareStatement(sql.toString());

			pstmt.setString(1, rota.getDescription());
			pstmt.setString(2, rota.getUser().getUsername());
			pstmt.setString(3, parse(rota.getCoords()));

			pstmt.execute();

			pstmt.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Route> find(Coordenada coordenada) {
		return null;
	}

	public Route load(Integer id) {
		return null;
	}

	private String parse(List<Coordenada> coordenadas) {
		String geometria = "LINESTRING(";
		int count = 0;
		for (Coordenada coordenada : coordenadas) {
			if (count != 0) {
				geometria += ", ";
			}
			geometria += coordenada.getLatitude() + " " + coordenada.getLongitude();
			count++;
		}
		geometria += ")";
		return geometria;
	}
}
