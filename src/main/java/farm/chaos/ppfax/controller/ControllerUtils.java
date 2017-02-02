package farm.chaos.ppfax.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;

import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;

public class ControllerUtils {

	public static void setStandardFields(HttpServletRequest request, UserService userService) {

	    request.setAttribute("loginUrl", userService.createLoginURL("/admin"));
	    request.setAttribute("logoutUrl", userService.createLogoutURL("/admin"));
	    request.setAttribute("googleUser", userService.getCurrentUser());
	    PpUser ppUser = Datastore.getPpUser(userService.getCurrentUser().getEmail());
		request.setAttribute("ppUser", ppUser);
		request.setAttribute("admin", PermissionService.checkRole(UserRole.ADMIN, ppUser.getRole()));
	}
}
