package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.persistance.Datastore;

@SuppressWarnings("serial")
public class AdminController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(AdminController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOG.log(Level.INFO, "User " + user.getUserId() + " - " + user.getEmail());


	    String action = request.getParameter("action");

	    if (action != null && action.equals("deleteArticle")) {
	    	long id = Long.parseLong(request.getParameter("id"));
	    	Article article = Datastore.getArticle(id);
	    	article.setStatus(PublicationStatus.DELETED);
	    	Datastore.saveArticle(article);
	    }

		response.sendRedirect("/admin");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {


	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOG.log(Level.INFO, "User " + user.getUserId() + " - " + user.getEmail());


		request.setAttribute("users", Datastore.getPpfaxUsers(null, 0, 10));

		RequestDispatcher rd = request.getRequestDispatcher("/admin.jsp");
		rd.forward(request, response);
	}

}
