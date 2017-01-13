package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Paragraph;
import farm.chaos.ppfax.model.ParagraphStyle;
import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class ArticleController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(ArticleController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		PpUser user = new PpUser();
		PermissionService.validatePermission(userService, UserRole.EDITOR, user);

	    String action = request.getParameter("action");
	    long articleId = StringUtils.atol(request.getParameter("id"));

	    if (action != null && articleId > 0 && action.equals("updateArticle")) {
	    	Article article = Datastore.getArticle(articleId);
	    	if (article != null) {
	    		article.setTitle(request.getParameter("title"));
	    		article.setHeadline(request.getParameter("headline"));
	    		article.setTeasertext(request.getParameter("teasertext"));
	    		article.setCategoryId(StringUtils.atol(request.getParameter("categoryId")));
	    		article.setKeywords(request.getParameter("keywords"));
	    		PublicationStatus oldStatus = article.getStatus();
	    		article.setStatus(PublicationStatus.valueOf(request.getParameter("status")));

		    	Date now = new Date();
		    	if (oldStatus != PublicationStatus.ONLINE && article.getStatus() == PublicationStatus.ONLINE)
	    			article.setDatePublished(now);
	    		article.setDateModified(now);

	    		LOG.log(Level.INFO, "Update " + article);
	    		Datastore.saveArticle(article);

	    		// update the paragraphs
	    		int pcount = StringUtils.atoi(request.getParameter("pcount"));
	    		for (int p = 1; p <= pcount; p++) {
	    			updateParagraph(request, p, articleId);
	    		}
	    	}
	    }
	    else if (action != null && action.equals("addArticle")) {
	    	Article article = new Article();
    		article.setTitle(request.getParameter("title"));
    		article.setHeadline(request.getParameter("headline"));
    		article.setTeasertext(request.getParameter("teasertext"));
    		article.setCategoryId(StringUtils.atol(request.getParameter("categoryId")));
    		article.setKeywords(request.getParameter("keywords"));
    		article.setStatus(PublicationStatus.valueOf(request.getParameter("status")));

	    	Date now = new Date();
    		article.setDateCreated(now);
    		article.setDateModified(now);
    		if (article.getStatus() == PublicationStatus.ONLINE)
    			article.setDatePublished(now);
    		article.setAuthorId(user.getId());
	    	LOG.log(Level.INFO, "Create " + article);
	    	Datastore.saveArticle(article);
	    }

	    response.sendRedirect("/article");
	}

	private void updateParagraph(HttpServletRequest request, int index, long articleId) {

		int sequence = StringUtils.atoi(request.getParameter("sequence_" + index));
		if (sequence == 0) return;

		long pid = StringUtils.atol(request.getParameter("paragraph_" + index));
		Paragraph paragraph = null;
		if (pid > 0) {
			paragraph = Datastore.getParagraph(pid);
		}
		Date now = new Date();
		if (paragraph == null) {
			paragraph = new Paragraph();
			paragraph.setDateCreated(now);
		}

		paragraph.setArticleId(articleId);
		paragraph.setDateModified(now);
		paragraph.setSequence(sequence);
		paragraph.setHeadline(request.getParameter("headline_" + index));
		paragraph.setBodyText(request.getParameter("bodyText_" + index));
		paragraph.setImageId(0L /* TODO: image */);
		paragraph.setStyle(ParagraphStyle.valueOf(request.getParameter("style_" + index)));

		Datastore.saveParagraph(paragraph);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		if (request.getUserPrincipal() == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			return;
		}

		PermissionService.validatePermission(userService, UserRole.READER, null);

		long id = StringUtils.getIdFromUri(request.getRequestURI());

		if (id > 0) {
			request.setAttribute("article", Datastore.getArticle(id));
			request.setAttribute("paragraphs", Datastore.getParagraphsForArticle(id));
		}

		if (request.getRequestURI().endsWith("/new"))
			request.setAttribute("newArticle", "1");

	    request.setAttribute("articles", Datastore.getArticles(null, 0, 10));
    	request.setAttribute("categories", Datastore.getCategories(null, 0, 10));
    	request.setAttribute("publicationStatus", PublicationStatus.values());
	    request.setAttribute("ParagraphStyles", ParagraphStyle.values());

		RequestDispatcher rd = request.getRequestDispatcher("/article.jsp");
		rd.forward(request, response);
	}

}
