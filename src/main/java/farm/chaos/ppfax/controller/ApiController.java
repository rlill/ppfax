package farm.chaos.ppfax.controller;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import farm.chaos.ppfax.model.Cronjob;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.security.PpfaxUserRole;
import farm.chaos.ppfax.security.Secured;
import farm.chaos.ppfax.security.SsoHelper;
import farm.chaos.ppfax.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;


@Api
@Secured
@Path("v1")
public class ApiController extends Application {

	private static final Logger LOG = Logger.getLogger(ApiController.class.getName());

	public ApiController() { }

    @GET
    @ApiOperation(value = "get article", response = Response.class, authorizations = {@Authorization(value = "Bearer")})
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticle(@PathParam("articleId") String articleId,
    		@Context SecurityContext securityContext) {

    	SsoHelper.checkUserAccess(PpfaxUserRole.EDITOR, securityContext, "getArticle");

    	long id = StringUtils.atol(articleId);
    	Cronjob j = Datastore.getCronjob(id);

    	if (j == null) return Response.status(Response.Status.NOT_FOUND).build();

    	return Response.ok().entity(j).build();
    }

    @POST
    @Secured
    @ApiOperation(value = "update article", response = Response.class, authorizations = {@Authorization(value = "Bearer")})
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArticle(
    		Cronjob job,
    		@Context SecurityContext securityContext) {

    	SsoHelper.checkUserAccess(PpfaxUserRole.EDITOR, securityContext, "getArticle");

    	Cronjob existing = Datastore.getCronjob(job.getId());
    	if (existing == null)
    		return Response.status(Response.Status.NOT_FOUND).build();

    	// TODO: update existing

    	Datastore.saveCronjob(existing);
        URI uri = URI.create("/v1/article/" + existing.getId());
    	return Response.created(uri).entity(existing).build();
    }

    @DELETE
    @Secured
    @ApiOperation(value = "delete article", response = Response.class, authorizations = {@Authorization(value = "Bearer")})
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJob(@PathParam("articleId") String articleId,
    		@Context SecurityContext securityContext) {

    	SsoHelper.checkUserAccess(PpfaxUserRole.MANAGER, securityContext, "getArticle");

    	long id = StringUtils.atol(articleId);
    	Cronjob j = Datastore.getCronjob(id);

    	if (j == null) return Response.status(Response.Status.NOT_FOUND).build();

    	Datastore.deleteCronjob(id);
    	return Response.ok().build();
    }

}
