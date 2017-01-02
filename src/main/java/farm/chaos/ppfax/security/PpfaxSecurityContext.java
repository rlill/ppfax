package farm.chaos.ppfax.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

public class PpfaxSecurityContext implements SecurityContext {

	private final SecurityContext securityContext;
	private final PpfaxPrincipal ppfaxPrincipal;
	private final String[] roles;

	public PpfaxSecurityContext(SecurityContext securityContext, PpfaxPrincipal ppfaxPrincipal,
							   String[] roles) {
		Validate.notNull(securityContext);
		this.securityContext = securityContext;

		Validate.notNull(ppfaxPrincipal);
		this.ppfaxPrincipal = ppfaxPrincipal;

		Validate.notEmpty(roles);
		for (String role : roles) {
			Validate.notBlank(role);
		}

		this.roles = roles;
	}

	public Principal getUserPrincipal() {
		return ppfaxPrincipal;
	}

	public boolean isUserInRole(String role) {
		// Must always be authenticated
		Validate.notBlank(role);
		return ArrayUtils.contains(roles, role);
	}

	public boolean isSecure() {
		return securityContext.isSecure();
	}

	public String getAuthenticationScheme() {
		return "PPFAX_AUTH";
	}
}
