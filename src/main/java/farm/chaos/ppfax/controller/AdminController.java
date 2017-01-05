package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.model.UserStatus;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class AdminController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(AdminController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		PermissionService.validatePermission(userService, UserRole.ADMIN);

	    String action = request.getParameter("action");
	    long id = StringUtils.atol(request.getParameter("id"));

	    if (action != null && action.equals("updateUser")) {
	    	PpUser user = Datastore.getPpUser(id);
	    	if (user != null) {
	    		user.setName(request.getParameter("name"));
	    		user.setRole(UserRole.valueOf(request.getParameter("role")));
	    		user.setStatus(UserStatus.valueOf(request.getParameter("status")));
	    		LOG.log(Level.INFO, "Update " + user);
	    		Datastore.savePpUser(user);
	    	}
	    }
	    else if (action != null && action.equals("addUser")) {
	    	PpUser user = new PpUser();
	    	user.setEmail(request.getParameter("email"));
	    	user.setName(request.getParameter("name"));
	    	user.setRole(UserRole.valueOf(request.getParameter("role")));
	    	user.setStatus(UserStatus.valueOf(request.getParameter("status")));
	    	LOG.log(Level.INFO, "Create " + user);
	    	Datastore.savePpUser(user);
	    }

	    response.sendRedirect("/admin");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		if (request.getUserPrincipal() == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}

		PermissionService.validatePermission(userService, UserRole.ADMIN);

    	ControllerUtils.setStandardFields(request, userService);

	    request.setAttribute("users", Datastore.getPpUsers(null, 0, 10));

		RequestDispatcher rd = request.getRequestDispatcher("/admin.jsp");
		rd.forward(request, response);
	}

}
