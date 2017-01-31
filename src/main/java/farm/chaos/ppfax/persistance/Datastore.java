package farm.chaos.ppfax.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.cmd.Query;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.model.Image;
import farm.chaos.ppfax.model.Paragraph;
import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.utils.StringUtils;

public class Datastore {

	private static final Logger LOG = Logger.getLogger(Datastore.class.getName());

	private static MemcacheService syncCache;

	private static Index indexArticles;
	private static Index indexImages;
	private static Index indexCategories;

	private static final String KEY_USER_EMAIL = "USER_EMAIL_";

	private static final String INDEX_NAME_ARTICLES = "PPFAX_ARTICLE";
	private static final String INDEX_NAME_IMAGES = "PPFAX_IMAGE";
	private static final String INDEX_NAME_CATEGORIES = "PPFAX_CATEGORY";

	private static final String INDEX_FIELD_TITLE = "TITLE";
	private static final String INDEX_FIELD_HEADLINE = "HEADLINE";
	private static final String INDEX_FIELD_MODIFIED = "DATE_MODIFIED";

	static {
    	NamespaceManager.set(null);
        syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));

        IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME_ARTICLES).build();
        indexArticles = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME_IMAGES).build();
        indexImages = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME_CATEGORIES).build();
        indexCategories = SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}

	// index
	private static void addToIndex(Index index, Document doc) {
		final int maxRetry = 3;
		int attempts = 0;
		int delay = 2;
		while (true) {
			try {
				index.put(doc);
			} catch (PutException e) {
				if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) { // retrying
					try { Thread.sleep(delay * 1000); } catch (InterruptedException f) { }
					delay *= 2; // easy exponential backoff
					continue;
				} else {
					throw e; // otherwise throw
				}
			}
			break;
		}
	}

	private static List<Long> searchIndex(Index index, String queryString, int offset, int limit) {
		final int maxRetry = 3;
		int attempts = 0;
		int delay = 2;
		List<Long> result = new ArrayList<>();
		while (true) {
			try {
				SortOptions sortOptions = SortOptions.newBuilder()
						.addSortExpression(SortExpression.newBuilder()
								.setExpression(INDEX_FIELD_MODIFIED)
								.setDirection(SortExpression.SortDirection.DESCENDING)
								.setDefaultValueNumeric(0))
						.setLimit(1000)
						.build();

				// Build the QueryOptions
				QueryOptions options = QueryOptions.newBuilder()
						.setLimit(limit)
						.setOffset(offset)
						.setFieldsToReturn()
						.setSortOptions(sortOptions)
						.build();

				// Build the Query and run the search
				com.google.appengine.api.search.Query query = com.google.appengine.api.search.Query.newBuilder()
						.setOptions(options)
						.build(queryString);

				Results<ScoredDocument> results = index.search(query);

				// Iterate over the documents in the results
				for (ScoredDocument document : results) {
					result.add(StringUtils.atol(document.getId()));
				}
			} catch (SearchException e) {
				if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) {
					// retry
					try { Thread.sleep(delay * 1000); } catch (InterruptedException e1) { }
					delay *= 2; // easy exponential backoff
					continue;
				} else {
					throw e;
				}
			}
			break;
		}
		return result;
	}

	// Users
	public static List<PpUser> getPpUsers(int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve all users");
		Query<PpUser> query = FeederObjectifyService.ofy().load().type(PpUser.class);
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
		PpUser user = (PpUser)syncCache.get(KEY_USER_EMAIL + email);
		LOG.log(Level.FINE, "Cache: " + user);
		if (user != null) return user;

		user = FeederObjectifyService.ofy().load().type(PpUser.class).filter("email", email).first().now();
		LOG.log(Level.FINE, "Datastore: " + user);
		if (user != null) syncCache.put(KEY_USER_EMAIL + email, user);
		return user;
	}

	public static void savePpUser(PpUser user) {
		LOG.log(Level.FINE, "Save PpUser with id = " + user.getId());
		FeederObjectifyService.ofy().save().entity(user).now();
		syncCache.put(KEY_USER_EMAIL + user.getEmail(), user);
	}

	// Articles
	public static List<Article> getArticles(int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve articles");
		Query<Article> query = FeederObjectifyService.ofy().load().type(Article.class);
		if (offset != 0) query = query.offset(offset);
		if (limit != 0) query = query.limit(limit);
		query = query.order("-dateModified");
		return query.list();
	}

	public static List<Article> searchArticles(String searchterm, int offset, int limit) {
		LOG.log(Level.FINE, "Search articles");
		List<Long> articleIds = searchIndex(indexArticles, searchterm, offset, limit);
		LOG.log(Level.INFO, "index search returned the IDs " +  articleIds);
		if (articleIds.size() < 1) return new ArrayList<>();
		Map<Long, Article> res =  FeederObjectifyService.ofy().load()
				.type(Article.class)
				.ids(articleIds);
		return new ArrayList<>(res.values());
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

        Document doc = Document.newBuilder()
            .setId(Long.toString(article.getId()))
            .addField(Field.newBuilder().setName(INDEX_FIELD_HEADLINE).setText(article.getHeadline()))
            .addField(Field.newBuilder().setName(INDEX_FIELD_TITLE).setText(article.getTitle()))
            .addField(Field.newBuilder().setName(INDEX_FIELD_MODIFIED).setDate(article.getDateModified()))
            .build();

		addToIndex(indexArticles, doc);
	}

	// Paragraphs
	public static List<Paragraph> getParagraphs(int offset, int limit) {
		LOG.log(Level.FINE, "Retrieve paragraphs");
		Query<Paragraph> query = FeederObjectifyService.ofy().load().type(Paragraph.class);
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

	public static List<Category> getSubCategories(long parentId) {
		LOG.log(Level.FINE, "Retrieve sub-categories of " + parentId);
		return FeederObjectifyService.ofy()
				.load()
				.type(Category.class)
				.filter("parentId", parentId)
				.list();
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
