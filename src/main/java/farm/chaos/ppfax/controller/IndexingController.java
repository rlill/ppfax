package farm.chaos.ppfax.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class IndexingController extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String contentType = request.getParameter("content-type");
		long id = StringUtils.atol(request.getParameter("id"));

		switch (contentType) {
		case "article":
			Datastore.updateArticleIndex(id);
			break;

//		case "category":
//			Datastore.updateCategoryIndex(id);
//			break;
//
//		case "image":
//			Datastore.updateImageIndex(id);
//			break;

		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		StringBuilder sb = new StringBuilder();
		sb.append("<form action=\"/indexing\" method=\"POST\">")
			.append("content-type: <input type=\"text\" name=\"content-type\" value=\"article\"><br>")
			.append("ID: <input type=\"text\" name=\"id\"><br>")
			.append("<input type=\"submit\">")
			.append("</form>");

		response.setContentType("text/html");
		response.getOutputStream().write(sb.toString().getBytes());
	}
}
