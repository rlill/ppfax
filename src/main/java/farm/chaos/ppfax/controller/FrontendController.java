package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.CategoryService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class FrontendController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(FrontendController.class.getName());

	private final static int ARTICLE_COUNT_OVERVIEW = 5;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String requestUri = request.getRequestURI();
		long id = StringUtils.getArticleIdFromUri(requestUri);

		if (id > 0) {

			// article page
			Article article = Datastore.getArticle(id);
			if (article != null) {

				String path = CategoryService.getArticlePath(article);
				if (!path.equals(requestUri)) {
					LOG.log(Level.INFO, "requestUri: " + requestUri + ", articleUri: " + path + ", redirecting");
				    response.sendRedirect(path);
				    return;
				}

				request.setAttribute("article", article);
				request.setAttribute("category", Datastore.getCategory(article.getCategoryId()));
				request.setAttribute("author", Datastore.getPpUser(article.getAuthorId()));

				RequestDispatcher rd = request.getRequestDispatcher("/articlepage.jsp");
				rd.forward(request, response);
				return;
			}

			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

		// topic page
		String path = requestUri;

		// insist in trailing slash
		if (!path.endsWith("/")) {
			path = path + "/";
		    response.sendRedirect(path);
		    return;
		}

		// now cut the trailing slash
		path = path.substring(0, path.length()-1);
		Category category = Datastore.getCategoryByPath(path);

		if (category == null) {
			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

		request.setAttribute("category", category);
		List<Article> articles = Datastore.getArticlesInCategory(category.getId(), 0, ARTICLE_COUNT_OVERVIEW);
		Category pcat = category;
		while (articles.size() < ARTICLE_COUNT_OVERVIEW && pcat.getParentId() > 0) {
			pcat = Datastore.getCategory(pcat.getParentId());
			articles.addAll(Datastore.getArticlesInCategory(pcat.getId(), 0, ARTICLE_COUNT_OVERVIEW - articles.size()));
		}
		request.setAttribute("articles", articles);

		RequestDispatcher rd = request.getRequestDispatcher("/topicpage.jsp");
		rd.forward(request, response);
	}

}
