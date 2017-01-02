package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import farm.chaos.ppfax.utils.CronProcessor;

@SuppressWarnings("serial")
public class CronjobController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(CronjobController.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		CronProcessor cronProcessor = new CronProcessor();

		cronProcessor.processCronJobs();

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
	    PrintWriter w = response.getWriter();
	    w.println("done");
	}
}
