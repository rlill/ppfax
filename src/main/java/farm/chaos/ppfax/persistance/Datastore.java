package farm.chaos.ppfax.persistance;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import farm.chaos.ppfax.model.CronLogLevel;
import farm.chaos.ppfax.model.Cronjob;
import farm.chaos.ppfax.model.LogEntry;

public class Datastore {

	private static final Logger LOG = Logger.getLogger(Datastore.class.getName());

	private static final String KEY_SECRET_TOKEN = "SECRET_TOKEN";

	private static MemcacheService syncCache;
	static {
    	NamespaceManager.set(null);
        syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}

	public static List<Cronjob> getAllCronjobs() {
		LOG.log(Level.FINE, "Retrieve all cronjobs");
		return FeederObjectifyService.ofy().load().type(Cronjob.class).list();
	}

	public static List<Cronjob> getAllCronjobs(String appId, String vendorId) {
		LOG.log(Level.FINE, "Retrieve all cronjobs");
		return FeederObjectifyService.ofy().load().type(Cronjob.class).filter("appId", appId).filter("vendorId", vendorId).list();
	}

	public static Cronjob getCronjob(long id) {
		LOG.log(Level.FINE, "Retrieve cronjob id=" + id);
		return FeederObjectifyService.ofy().load().type(Cronjob.class).id(id).now();
	}

	public static void deleteCronjob(long id) {
		LOG.log(Level.FINE, "Delete cronjob id=" + id);
		FeederObjectifyService.ofy().delete().type(Cronjob.class).id(id).now();
	}

	public static void saveCronjob(Cronjob cronjob) {
		LOG.log(Level.FINE, "Save Cronjob with id = " + cronjob.getId());
		FeederObjectifyService.ofy().save().entity(cronjob).now();
	}

	public static void saveLogEntry(LogEntry logEntry) {
		LOG.log(Level.FINE, "Save log entry");
		FeederObjectifyService.ofy().save().entity(logEntry).now();
	}

	public static List<LogEntry> getLogEntries(int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve log entries");
		return FeederObjectifyService.ofy().load().type(LogEntry.class).order("-timestamp").offset(offset).limit(limit).list();
	}

	public static void saveLogEntry(CronLogLevel level, String message) {
		LogEntry logEntry = new LogEntry();
		logEntry.setTimestamp(new Date());
		logEntry.setMessage(message);
		logEntry.setLevel(level);
		LOG.log(Level.FINE, "Save log entry");
		FeederObjectifyService.ofy().save().entity(logEntry).now();
	}

	// Secret Token

	public static String getSecretToken() {
		String token = (String)syncCache.get(KEY_SECRET_TOKEN);
		if (token == null) {
			token = UUID.randomUUID().toString();
			syncCache.put(KEY_SECRET_TOKEN, token);
		}
		return token;
	}

}
