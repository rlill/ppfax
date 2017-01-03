package farm.chaos.ppfax.controller;

import java.net.URI;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api
@Path("v1")
public class ApiController extends Application {

	private static final Logger LOG = Logger.getLogger(ApiController.class.getName());

	public ApiController() { }

    @GET
    @ApiOperation(value = "get article", response = Response.class)
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticle(@PathParam("articleId") String articleId) {

	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOG.log(Level.INFO, "User " + user.getUserId() + " - " + user.getEmail());

    	long id = StringUtils.atol(articleId);
    	Article a = Datastore.getArticle(id);

    	if (a == null) return Response.status(Response.Status.NOT_FOUND).build();

    	return Response.ok().entity(a).build();
    }

    @POST
    @ApiOperation(value = "update article", response = Response.class)
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArticle(Article article) {

	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOG.log(Level.INFO, "User " + user.getUserId() + " - " + user.getEmail());

//    	SsoHelper.checkUserAccess(UserRole.EDITOR, securityContext, "updateArticle");

    	Article existing = Datastore.getArticle(article.getId());
    	if (existing == null)
    		return Response.status(Response.Status.NOT_FOUND).build();

//    	if (existing.getAuthorId() != ppfaxUser.getId())
//        	SsoHelper.checkUserAccess(UserRole.MANAGER, securityContext, "updateArticle");

    	// update dedicated fields
    	existing.setHeadline(article.getHeadline());
    	existing.setTitle(article.getTitle());
    	existing.setTeasertext(article.getTeasertext());
    	existing.setKeywords(article.getKeywords());
    	existing.setStatus(article.getStatus());
    	existing.setCategoryId(article.getCategoryId());

    	existing.setDateModified(new Date());

    	Datastore.saveArticle(existing);
        URI uri = URI.create("/v1/article/" + existing.getId());
    	return Response.created(uri).entity(existing).build();
    }

    @DELETE
    @ApiOperation(value = "delete article", response = Response.class)
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteArticle(@PathParam("articleId") String articleId) {

	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOG.log(Level.INFO, "User " + user.getUserId() + " - " + user.getEmail());

    	long id = StringUtils.atol(articleId);
    	Article article = Datastore.getArticle(id);

    	if (article == null)
    		return Response.status(Response.Status.NOT_FOUND).build();

    	article.setStatus(PublicationStatus.DELETED);
    	Datastore.saveArticle(article);

    	return Response.ok().build();
    }

}
