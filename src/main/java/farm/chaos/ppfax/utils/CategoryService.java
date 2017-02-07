package farm.chaos.ppfax.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.persistance.Datastore;

public class CategoryService {

	private static final Logger LOG = Logger.getLogger(CategoryService.class.getName());

	private static final int MAX_CATEGORY_DEPTH = 10;

	public static String getCategoryPath(Category cat) {

		String path = rightPath(cat.getPath());

		int maxdepth = MAX_CATEGORY_DEPTH;
		while (cat.getParentId() != null && cat.getParentId() != 0) {
			cat = Datastore.getCategory(cat.getParentId());
			path = rightPath(cat.getPath()) + path;
			if (--maxdepth == 0) throw new IllegalArgumentException("Category path deeper than " + MAX_CATEGORY_DEPTH + " levels or recursion");
		}

		String p = path.toLowerCase();
		LOG.log(Level.INFO, "Path: " + p);
		return p;
	}

	public static String rightPath(String fullPath) {
		int p = fullPath.lastIndexOf('/');
		while (p > 0 && p >= fullPath.length() - 1) {
			fullPath = fullPath.substring(0, p-1);
			p = fullPath.lastIndexOf('/');
		}
		if (p == -1)
			return "/" + fullPath;
		return "/" + normalizePath(fullPath.substring(p+1));
	}

	public static String normalizePath(String path) {
		return path.toLowerCase()
				.replace("&", "-and-")
				.replace("€", "-euro-")
				.replace("$", "-dollar-")
				.replace("ä", "ae")
				.replace("ö", "oe")
				.replace("ü", "ue")
				.replace("ß", "ss")
				.replaceAll("[^a-z0-9]", "-")
				.replaceAll("--+", "-")
				.replaceAll("^-", "")
				.replaceAll("-$", "");
	}

	public static String getArticlePath(Article article) {
		StringBuilder path = new StringBuilder();
		Category category = Datastore.getCategory(article.getCategoryId());
		path.append(category.getPath())
			.append('/')
			.append(normalizePath(article.getTitle() + "-" + article.getHeadline()))
			.append('-')
			.append(article.getId())
			.append(".html");
		return path.toString();
	}

	public static void fixSubcategoryPaths(long categoryId) {
		LOG.log(Level.INFO, "Fix paths for subcategories of " + categoryId);
		for (Category subcat : Datastore.getSubCategories(categoryId)) {
			String newPath = getCategoryPath(subcat);
			if (!newPath.equals(subcat.getPath())) {
				subcat.setPath(newPath);
				Datastore.saveCategory(subcat);
				fixSubcategoryPaths(subcat.getId());
			}
		}
	}
}
