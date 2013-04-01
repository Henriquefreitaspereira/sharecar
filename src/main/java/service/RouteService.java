package service;

import static service.Constants.JSON_MEDIA_TYPE;

import java.sql.Time;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import business.RouteBC;
import entity.Coordinate;
import entity.Route;
import entity.User;
import entity.Weekday;

@Path("/route")
public class RouteService {

	@Inject
	private RouteBC routeBC;

	@PUT
	@Consumes(JSON_MEDIA_TYPE)
	public void create(Route route) throws Exception {
		routeBC.insert(route, getCurrentUser());
	}

	@GET
	@LoggedIn
	@Produces(JSON_MEDIA_TYPE)
	public List<Route> findAll() throws Exception {
		return routeBC.find(getCurrentUser());
	}

	@GET
	@Path("/{id}")
	@Produces(JSON_MEDIA_TYPE)
	public Route find(@PathParam("id") Integer id) throws Exception {
		return routeBC.load(id);
	}

	@DELETE
	@Path("/{id}")
	@Produces(JSON_MEDIA_TYPE)
	public void delete(@PathParam("id") Integer id) throws Exception {
		Route route = new Route();
		route.setId(id);
		routeBC.delete(route);
	}

	@GET
	@Produces(JSON_MEDIA_TYPE)
	public List<Route> find(@QueryParam("lat") Double latitude, @QueryParam("lng") Double longitude,
			@QueryParam("radius") Integer radius, @QueryParam("weekday") Integer weekday,
			@QueryParam("hourini") Time hourini, @QueryParam("hourend") Time hourend) throws Exception {
		return routeBC.find(new Coordinate(latitude, longitude), radius, getCurrentUser(), Weekday.valueOf(weekday),
				hourini, hourend);
	}

	private User getCurrentUser() {
		return (User) Beans.getReference(SecurityContext.class).getCurrentUser();
	}
}
