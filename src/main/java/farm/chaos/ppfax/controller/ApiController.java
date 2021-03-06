package farm.chaos.ppfax.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Image;
import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;
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
    public Response getArticle(@PathParam("articleId") String articleId) throws ServletException {

    	LOG.log(Level.INFO, "getArticle(" + articleId + ")");

		UserService userService = UserServiceFactory.getUserService();
		PermissionService.validatePermission(userService, UserRole.READER);

    	long id = StringUtils.atol(articleId);
    	Article a = Datastore.getArticle(id);

    	if (a == null) return Response.status(Response.Status.NOT_FOUND).build();

    	return Response.ok().entity(a).build();
    }

    @PUT
    @ApiOperation(value = "create article", response = Response.class)
    @Path("/article/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArticle(Article article) throws ServletException {

    	LOG.log(Level.INFO, "createArticle()");

		UserService userService = UserServiceFactory.getUserService();
		PpUser user = new PpUser();
    	PermissionService.validatePermission(userService, UserRole.EDITOR, user);

    	Article newArticle = new Article();

    	// update dedicated fields
    	newArticle.setHeadline(article.getHeadline());
    	newArticle.setTitle(article.getTitle());
    	newArticle.setTeasertext(article.getTeasertext());
    	newArticle.setKeywords(article.getKeywords());
    	newArticle.setStatus(article.getStatus());
    	newArticle.setCategoryId(article.getCategoryId());

    	newArticle.setDateCreated(new Date());
    	newArticle.setDateModified(new Date());
    	newArticle.setAuthorId(user.getId());

    	Datastore.saveArticle(newArticle);
    	URI uri = URI.create("/v1/article/" + newArticle.getId());
    	return Response.created(uri).entity(newArticle).build();
    }

    @POST
    @ApiOperation(value = "update article", response = Response.class)
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArticle(Article article) throws ServletException {

    	LOG.log(Level.INFO, "updateArticle(" + article.getId() + ")");

		UserService userService = UserServiceFactory.getUserService();
    	PermissionService.validatePermission(userService, UserRole.EDITOR);

    	Article existing = Datastore.getArticle(article.getId());
    	if (existing == null)
    		return Response.status(Response.Status.NOT_FOUND).build();

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
    public Response deleteArticle(@PathParam("articleId") String articleId) throws ServletException {

    	LOG.log(Level.INFO, "deleteArticle(" + articleId + ")");

		UserService userService = UserServiceFactory.getUserService();
    	PermissionService.validatePermission(userService, UserRole.EDITOR);

    	long id = StringUtils.atol(articleId);
    	Article article = Datastore.getArticle(id);

    	if (article == null)
    		return Response.status(Response.Status.NOT_FOUND).build();

    	article.setStatus(PublicationStatus.DELETED);
    	Datastore.saveArticle(article);

    	return Response.ok().build();
    }

    @GET
    @ApiOperation(value = "search images", response = Response.class)
    @Path("/image/search/{searchterm}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchImage(@PathParam("searchterm") String searchterm) throws ServletException {

    	LOG.log(Level.INFO, "searchImage(" + searchterm + ")");

		UserService userService = UserServiceFactory.getUserService();
		PermissionService.validatePermission(userService, UserRole.READER);

		List<Image> result;
		if (searchterm == null || searchterm.isEmpty() || searchterm.equals("*"))
			result = Datastore.getImages(0, 10);
		else
			result = Datastore.searchImages(searchterm, 0, 10);

    	if (result.size() < 1) return Response.status(Response.Status.NOT_FOUND).build();

    	return Response.ok().entity(result).build();
    }

}
