package farm.chaos.ppfax.security;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.SecurityContext;

import farm.chaos.ppfax.model.UserRole;

public class SsoHelper {

	private static final Logger LOG = Logger.getLogger(SsoHelper.class.getName());

	public static void checkUserAccess(UserRole role, SecurityContext securityContext, String method) {

		final PpfaxPrincipal principal = (PpfaxPrincipal) securityContext.getUserPrincipal();
		final String vendorIdFromSSO = principal.getCustomerName();
		LOG.log(Level.FINE, "checkUserAccess(" + role + ", " + method + ") vendorIdFromSSO = " + vendorIdFromSSO);

		if (!"".equals(vendorIdFromSSO)) {
			throw new NotAuthorizedException("Customer \"" + vendorIdFromSSO + "\" is not authorised to access " + method);
		}
	}

}
