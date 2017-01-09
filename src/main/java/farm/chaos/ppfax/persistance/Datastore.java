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
import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.model.Image;
import farm.chaos.ppfax.model.Paragraph;
import farm.chaos.ppfax.model.PpUser;

public class Datastore {

	private static final Logger LOG = Logger.getLogger(Datastore.class.getName());

	private static MemcacheService syncCache;

	private static final String KEY_USER_EMAIL = "USER_EMAIL_";

	static {
    	NamespaceManager.set(null);
        syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}

	// Users
	public static List<PpUser> getPpUsers(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve all users");
		Query<PpUser> query = FeederObjectifyService.ofy().load().type(PpUser.class);
		if (searchterm != null) query = query.filter("", searchterm); // TODO
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		return query.list();
	}

	public static PpUser getPpUser(long id) {
		LOG.log(Level.FINE, "Retrieve user id=" + id);
		return FeederObjectifyService.ofy().load().type(PpUser.class).id(id).now();
	}

	public static PpUser getPpUser(String email) {
		LOG.log(Level.FINE, "Retrieve user email=" + email);
		LOG.log(Level.INFO, "Retrieve user email=" + email);
		PpUser user = (PpUser)syncCache.get(KEY_USER_EMAIL + email);
		LOG.log(Level.INFO, "Cache: " + user);
		if (user != null) return user;

		user = FeederObjectifyService.ofy().load().type(PpUser.class).filter("email", email).first().now();
		LOG.log(Level.INFO, "Datastore: " + user);
		if (user != null) syncCache.put(KEY_USER_EMAIL + email, user);
		return user;
	}

	public static void savePpUser(PpUser user) {
		LOG.log(Level.FINE, "Save PpUser with id = " + user.getId());
		FeederObjectifyService.ofy().save().entity(user).now();
		syncCache.put(KEY_USER_EMAIL + user.getEmail(), user);
	}

	// Articles
	public static List<Article> getArticles(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve articles");
		Query<Article> query = FeederObjectifyService.ofy().load().type(Article.class);
		if (searchterm != null) query = query.filter("", ""); // TODO
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		query = query.order("-dateModified");
		return query.list();
	}

	public static List<Article> getArticlesInCategory(Long categoryId, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve articles");
		Query<Article> query = FeederObjectifyService.ofy().load().type(Article.class);
		query = query.filter("categoryId", categoryId);
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		query = query.order("-dateModified");
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

	// Paragraphs
	public static List<Paragraph> getParagraphs(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve paragraphs");
		Query<Paragraph> query = FeederObjectifyService.ofy().load().type(Paragraph.class);
		if (searchterm != null) query = query.filter("", ""); // TODO
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		return query.list();
	}

	public static Paragraph getParagraph(long id) {
		LOG.log(Level.FINE, "Retrieve paragraph id=" + id);
		return FeederObjectifyService.ofy().load().type(Paragraph.class).id(id).now();
	}

	public static List<Paragraph> getParagraphsForArticle(long articleId) {
		LOG.log(Level.FINE, "Retrieve paragraphs");
		return FeederObjectifyService.ofy()
				.load()
				.type(Paragraph.class)
				.filter("articleId", articleId)
				.order("sequence")
				.list();
	}

	public static void saveParagraph(Paragraph paragraph) {
		LOG.log(Level.FINE, "Save Paragraph id = " + paragraph.getId());
		FeederObjectifyService.ofy().save().entity(paragraph).now();
	}

	// Categories
	public static List<Category> getCategories(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve categories");
		Query<Category> query = FeederObjectifyService.ofy().load().type(Category.class);
		if (searchterm != null) query = query.filter("", ""); // TODO
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		query = query.order("path");
		return query.list();
	}

	public static Category getCategory(long id) {
		LOG.log(Level.FINE, "Retrieve category id=" + id);
		return FeederObjectifyService.ofy().load().type(Category.class).id(id).now();
	}

	public static Category getCategoryByPath(String path) {
		LOG.log(Level.FINE, "Retrieve category path=" + path);
		return FeederObjectifyService.ofy().load().type(Category.class).filter("path", path).first().now();
	}

	public static void saveCategory(Category category) {
		LOG.log(Level.FINE, "Save category id=" + category.getId());
		FeederObjectifyService.ofy().save().entity(category).now();
	}

	// Images
	public static List<Image> getImages(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve images");
		Query<Image> query = FeederObjectifyService.ofy().load().type(Image.class);
		if (searchterm != null) query = query.filter("", ""); // TODO
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		return query.list();
	}

	public static Image getImage(long id) {
		LOG.log(Level.FINE, "Retrieve image id=" + id);
		return FeederObjectifyService.ofy().load().type(Image.class).id(id).now();
	}

	public static void saveImage(Image image) {
		LOG.log(Level.FINE, "Save Image id = " + image.getId());
		FeederObjectifyService.ofy().save().entity(image).now();
	}

}
