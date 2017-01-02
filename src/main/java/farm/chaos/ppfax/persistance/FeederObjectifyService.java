package farm.chaos.ppfax.persistance;

import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.utils.SystemProperty;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import farm.chaos.ppfax.model.Cronjob;
import farm.chaos.ppfax.model.LogEntry;


public class FeederObjectifyService {

	private static final Logger LOG = Logger.getLogger(FeederObjectifyService.class.getName());

	private final static String namespace;
    static {
		ObjectifyService.register(Cronjob.class);
		ObjectifyService.register(LogEntry.class);

    	String ns = SystemProperty.applicationVersion.get();
    	if (ns != null) {
	    	int p = ns.indexOf('.');
	    	if (p > 0) ns = ns.substring(0, p);
	    	if (ns.charAt(0) == '$') ns = null;
    	}
    	LOG.info("Datastore namespace: \"" + ns + "\"");
		namespace = ns;
    }

    public static Objectify ofy() {
    	NamespaceManager.set(namespace);
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
    	NamespaceManager.set(namespace);
        return ObjectifyService.factory();
    }

}
