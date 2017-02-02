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

import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.CategoryService;
import farm.chaos.ppfax.utils.PermissionService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class CategoryController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(CategoryController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		PpUser user = new PpUser();
		PermissionService.validatePermission(userService, UserRole.MANAGER, user);

	    String action = request.getParameter("action");
	    long id = StringUtils.atol(request.getParameter("id"));

	    if (action != null && id > 0 && action.equals("updateCategory")) {
	    	Category category = Datastore.getCategory(id);
	    	if (category != null) {
	    		category.setName(request.getParameter("name"));
	    		category.setSidebarcontent(request.getParameter("sidebarcontent"));
	    		category.setKeywords(request.getParameter("keywords"));
	    		category.setParentId(StringUtils.atol(request.getParameter("parentId")));
	    		category.setStatus(PublicationStatus.valueOf(request.getParameter("status")));

	    		String newPath = request.getParameter("path");
	    		LOG.log(Level.INFO, "old: " + category.getPath() + ", new: " + newPath);
	    		if (!category.getPath().equals(newPath)) {
		    		// set path first to user-input, then correct
		    		category.setPath(newPath);
		    		category.setPath(CategoryService.getCategoryPath(category));

		    		// fix path for all sub-categories
		    		CategoryService.fixSubcategoryPaths(id);
	    		}

	    		category.setDateModified(new Date());

	    		LOG.log(Level.INFO, "Update " + category);
	    		Datastore.saveCategory(category);
	    	}
	    }
	    else if (action != null && action.equals("addCategory")) {
	    	Category category = new Category();
	    	category.setName(request.getParameter("name"));
    		category.setSidebarcontent(request.getParameter("sidebarcontent"));
    		category.setKeywords(request.getParameter("keywords"));
    		category.setParentId(StringUtils.atol(request.getParameter("parentId")));
    		category.setStatus(PublicationStatus.valueOf(request.getParameter("status")));

    		// set path first to user-input, then correct
    		category.setPath(request.getParameter("path"));
    		category.setPath(CategoryService.getCategoryPath(category));

	    	Date now = new Date();
	    	category.setDateCreated(now);
	    	category.setDateModified(now);
	    	category.setAuthorId(user.getId());

	    	LOG.log(Level.INFO, "Create " + category);
	    	Datastore.saveCategory(category);
	    }

	    response.sendRedirect("/category");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		if (request.getUserPrincipal() == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}

		PermissionService.validatePermission(userService, UserRole.READER, null);
    	ControllerUtils.setStandardFields(request, userService);

		long id = StringUtils.getIdFromUri(request.getRequestURI());
		if (id > 0) {
			request.setAttribute("category", Datastore.getCategory(id));
		}

		if (request.getRequestURI().endsWith("/new"))
			request.setAttribute("newCategory", "1");

		String searchString = request.getParameter("searchstring");
		if (searchString != null && searchString.length() > 2) {
	    	request.setAttribute("categories", Datastore.searchCategories(searchString, 0, 10));
		}
		else {
	    	request.setAttribute("categories", Datastore.getCategories(0, 10));
		}

	    request.setAttribute("publicationStatus", PublicationStatus.values());

		RequestDispatcher rd = request.getRequestDispatcher("/category.jsp");
		rd.forward(request, response);
	}

}
