package farm.chaos.ppfax.persistance;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.cmd.Query;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.PpUser;

public class Datastore {

	private static final Logger LOG = Logger.getLogger(Datastore.class.getName());

	private static MemcacheService syncCache;

	static {
    	NamespaceManager.set(null);
        syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}

	// Users
	public static List<PpUser> getPpfaxUsers(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve all users");
		return FeederObjectifyService.ofy().load().type(PpUser.class).offset(offset).limit(limit).list();
	}

	public static PpUser getPpfaxUser(long id) {
		LOG.log(Level.FINE, "Retrieve user id=" + id);
		return FeederObjectifyService.ofy().load().type(PpUser.class).id(id).now();
	}

	public static void savePpfaxUser(PpUser user) {
		LOG.log(Level.FINE, "Save PpfaxUser with id = " + user.getId());
		FeederObjectifyService.ofy().save().entity(user).now();
	}

	// Articles
	public static List<Article> getArticles(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve articles");
		Query<Article> query = FeederObjectifyService.ofy().load().type(Article.class);

		if (searchterm != null)
			query = query.filter("", "");

		if (offset != 0)
			query = query.offset(offset);

		if (limit != 0)
			query = query.limit(limit);

		return query.list();
	}

	public static Article getArticle(long id) {
		LOG.log(Level.FINE, "Retrieve article id=" + id);
		return FeederObjectifyService.ofy().load().type(Article.class).id(id).now();
	}

	public static void saveArticle(Article article) {
		LOG.log(Level.FINE, "Save Article id = " + article.getId());
		FeederObjectifyService.ofy().save().entity(article).now();
	}

	// Categories

}
