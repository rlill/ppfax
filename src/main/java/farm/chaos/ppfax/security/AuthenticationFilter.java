package farm.chaos.ppfax.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * See <a href="http://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey">
 * http://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
 * </a>
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

    public static final String AUTH_HEADER = "Authorization";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

    	// CORS OPTIONS requests do not require authentication
        if (requestContext.getMethod() == HttpMethod.OPTIONS)
            return;

        try {
            // Get the HTTP Authorization header from the request
            String authorizationHeader = requestContext.getHeaderString(AUTH_HEADER);
            // Check if the Authorization header is present and formatted correctly
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new NotAuthorizedException("Authorization header must be provided");
            }

            // Extract the token from the Authorization header
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            Validate.isTrue(StringUtils.isNotBlank(token));

            // TODO: Validate the token
//            ValidationResponse validationResponse = validateToken(token);
            // Set our own extended version of the security context
//            PpfaxPrincipal principal = new PpfaxPrincipal(validationResponse.getPreferredUsername(), validationResponse.getCustomerName());
            PpfaxPrincipal principal = new PpfaxPrincipal("", "");

            PpfaxSecurityContext ppfaxSecurityContext = new PpfaxSecurityContext(requestContext.getSecurityContext(), principal, null);
            requestContext.setSecurityContext(ppfaxSecurityContext);

        } catch (Exception e) {
            LOG.log(Level.INFO, e.getMessage(), e);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .build());
        }

    }

}
