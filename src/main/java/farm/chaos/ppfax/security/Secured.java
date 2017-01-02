package farm.chaos.ppfax.security;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured { }
