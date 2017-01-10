package farm.chaos.ppfax.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ForbiddenException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.model.UserStatus;
import farm.chaos.ppfax.persistance.Datastore;

public class PermissionService {

	private static final Logger LOG = Logger.getLogger(PermissionService.class.getName());

	public static void validatePermission(UserService userService, UserRole requiredRole) throws ForbiddenException {
		validatePermission(userService, requiredRole, null);
	}

	public static void validatePermission(UserService userService, UserRole requiredRole, PpUser rUser) throws ForbiddenException {

	    User user = userService.getCurrentUser();
	    if (user == null) {
	    	LOG.log(Level.INFO, "No current user, not logged in?");
	    	throw new ForbiddenException();
	    }
    	LOG.log(Level.FINE, "User " + user.getUserId() + " - " + user.getEmail());

		PpUser ppUser = Datastore.getPpUser(user.getEmail());
		if (ppUser == null) {
			LOG.log(Level.INFO, "No user entry found in datastore for email=" + user.getEmail());

			ppUser = new PpUser();
			ppUser.setEmail(user.getEmail());
			ppUser.setRole(UserRole.READER);
			ppUser.setStatus(UserStatus.REQUESTED);
			Datastore.savePpUser(ppUser);

			if (!userService.isUserAdmin()) throw new ForbiddenException();
		}
    	LOG.log(Level.FINE, ppUser.toString());

    	if (rUser != null) {
    		rUser.setId(ppUser.getId());
    		rUser.setName(ppUser.getName());
    	}

		// Admin can do anything
		if (userService.isUserAdmin()) {
			LOG.log(Level.FINE, "User is GCP project admin");
			return;
		}

		if (ppUser.getStatus() != UserStatus.ACTIVE) {
			LOG.log(Level.INFO, "User has status " + ppUser.getStatus());
			throw new ForbiddenException();
		}

		if (!checkRole(requiredRole, ppUser.getRole())) {
			LOG.log(Level.INFO, "User has role " + ppUser.getRole() + ", but required is " + requiredRole);
			throw new ForbiddenException();
		}
	}

	public static boolean checkRole(UserRole requiredRole, UserRole availableRole) {

		switch(requiredRole) {
		case READER:
			return (availableRole == UserRole.ADMIN || availableRole == UserRole.MANAGER
			|| availableRole == UserRole.EDITOR || availableRole == UserRole.READER);
		case EDITOR:
			return (availableRole == UserRole.ADMIN || availableRole == UserRole.MANAGER
			|| availableRole == UserRole.EDITOR);
		case MANAGER:
			return (availableRole == UserRole.ADMIN || availableRole == UserRole.MANAGER);
		case ADMIN:
			return (availableRole == UserRole.ADMIN);
		}
		return false;
	}
}
