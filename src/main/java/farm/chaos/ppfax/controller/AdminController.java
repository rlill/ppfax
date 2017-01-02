package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import farm.chaos.ppfax.model.Cronjob;
import farm.chaos.ppfax.model.CronjobStatus;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.CronProcessor;
import farm.chaos.ppfax.utils.ImproperCronValueException;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class AdminController extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	    String action = request.getParameter("action");

	    if (action != null && action.equals("addjob")) {
	    	Cronjob cronjob = new Cronjob();
	    	String msg = saveCronjob(request, cronjob);

	    	if (msg != null) {
	    		prepareErrorPage(request, response, msg);
	    		return;
	    	}
	    }
	    else if (action != null && action.equals("updatejob")) {
	    	long id = Long.parseLong(request.getParameter("id"));
	    	Cronjob cronjob = Datastore.getCronjob(id);
	    	if (cronjob == null) {
	    		prepareErrorPage(request, response, "id not found");
	    		return;
	    	}
	    	String msg = saveCronjob(request, cronjob);

	    	if (msg != null) {
	    		prepareErrorPage(request, response, msg);
	    		return;
	    	}
	    }
	    else if (action != null && action.equals("deletejob")) {
	    	long id = Long.parseLong(request.getParameter("id"));
	    	Datastore.deleteCronjob(id);
	    }

		response.sendRedirect("/admin");
	}

	private void prepareErrorPage(HttpServletRequest request, HttpServletResponse response, String msg)
			throws ServletException, IOException {

		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("minute", request.getParameter("minute"));
		request.setAttribute("hour", request.getParameter("hour"));
		request.setAttribute("dayofmonth", request.getParameter("dayofmonth"));
		request.setAttribute("month", request.getParameter("month"));
		request.setAttribute("year", request.getParameter("year"));
		request.setAttribute("dayofweek", request.getParameter("dayofweek"));
		request.setAttribute("maxruntime", request.getParameter("maxruntime"));
		request.setAttribute("command", request.getParameter("command"));
		request.setAttribute("notification", request.getParameter("notification"));
		request.setAttribute("status", request.getParameter("status"));
		request.setAttribute("appId", request.getParameter("appId"));
		request.setAttribute("vendorId", request.getParameter("vendorId"));
		request.setAttribute("errormessage", msg);

		RequestDispatcher rd = request.getRequestDispatcher("/admin.jsp");
		rd.forward(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setAttribute("jobs", Datastore.getAllCronjobs());
		request.setAttribute("modify", request.getParameter("modify"));
		request.setAttribute("currenttime", new Date());
		request.setAttribute("jobstatus", CronjobStatus.values());

		RequestDispatcher rd = request.getRequestDispatcher("/admin.jsp");
		rd.forward(request, response);
	}

	private String saveCronjob(HttpServletRequest request, Cronjob cronjob) {

    	cronjob.setMinute(request.getParameter("minute"));
    	cronjob.setHour(request.getParameter("hour"));
    	cronjob.setDayOfMonth(request.getParameter("dayofmonth"));
    	cronjob.setMonth(request.getParameter("month"));
    	cronjob.setYear(request.getParameter("year"));
    	cronjob.setDayOfWeek(request.getParameter("dayofweek"));
    	cronjob.setMaxRunMinutes(StringUtils.atoi(request.getParameter("maxruntime")));
    	cronjob.setCommand(request.getParameter("command"));
    	cronjob.setNotification(request.getParameter("notification"));
    	cronjob.setStatus(CronjobStatus.getInstanceByCode(request.getParameter("status")));
    	cronjob.setAppId(request.getParameter("appId"));
    	cronjob.setVendorId(request.getParameter("vendorId"));

    	String msg = CronProcessor.verifyParameters(cronjob);
    	if (msg != null) return msg;

    	try {
			Date next = CronProcessor.calcNextRun(cronjob, new Date());
			cronjob.setNextRun(next);
		} catch (ImproperCronValueException e) {
			return "Error calculating next run time: " + e.getMessage();
		}

    	Datastore.saveCronjob(cronjob);

    	return null;
	}

}
