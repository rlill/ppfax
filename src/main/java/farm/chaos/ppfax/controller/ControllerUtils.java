package farm.chaos.ppfax.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;

import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.model.UserStatus;
import farm.chaos.ppfax.persistance.Datastore;

public class ControllerUtils {

	public static void setStandardFields(HttpServletRequest request, UserService userService) {

	    request.setAttribute("loginUrl", userService.createLoginURL("/admin"));
	    request.setAttribute("logoutUrl", userService.createLogoutURL("/admin"));
	    request.setAttribute("googleUser", userService.getCurrentUser());
		request.setAttribute("ppUser", Datastore.getPpUser(userService.getCurrentUser().getEmail()));

		request.setAttribute("userRoles", UserRole.values());
		request.setAttribute("userStatus", UserStatus.values());
	}
}