package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class ArticleController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(ArticleController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		PermissionService.validatePermission(userService, UserRole.EDITOR);

	    String action = request.getParameter("action");
	    long id = StringUtils.atol(request.getParameter("id"));

	    if (action != null && action.equals("updateArticle")) {
	    	Article article = Datastore.getArticle(id);
	    	if (article != null) {
	    		article.setTitle(request.getParameter("title"));
	    		article.setDateModified(new Date());
	    		LOG.log(Level.INFO, "Update " + article);
	    		Datastore.saveArticle(article);
	    	}
	    }
	    else if (action != null && action.equals("addArticle")) {
	    	Article article = new Article();
	    	article.setTitle(request.getParameter("title"));
	    	Date now = new Date();
    		article.setDateCreated(now);
    		article.setDateModified(now);
	    	LOG.log(Level.INFO, "Create " + article);
	    	Datastore.saveArticle(article);
	    }

	    response.sendRedirect("/article");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		if (request.getUserPrincipal() == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}

		PermissionService.validatePermission(userService, UserRole.READER);

		long id = StringUtils.getIdFromUri(request.getRequestURI());

		if (id > 0) {
			request.setAttribute("article", Datastore.getArticle(id));
			request.setAttribute("paragraphs", Datastore.getParagraphsForArticle(id));
			// TODO: Images !??
		}

    	ControllerUtils.setStandardFields(request, userService);

	    request.setAttribute("articles", Datastore.getArticles(null, 0, 10));

		RequestDispatcher rd = request.getRequestDispatcher("/article.jsp");
		rd.forward(request, response);
	}

}
