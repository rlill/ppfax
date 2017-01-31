package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import farm.chaos.ppfax.model.Image;
import farm.chaos.ppfax.model.PpUser;
import farm.chaos.ppfax.model.PublicationStatus;
import farm.chaos.ppfax.model.UserRole;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.PermissionService;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class ImageController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(ImageController.class.getName());

    private final GcsService gcsService = GcsServiceFactory.createGcsService(
    		new RetryParams.Builder()
				    .initialRetryDelayMillis(10)
				    .retryMaxAttempts(10)
				    .totalRetryPeriodMillis(15000)
				    .build());

    private String bucketName = AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();
		PpUser user = new PpUser();
		PermissionService.validatePermission(userService, UserRole.EDITOR, user);

	    String action = request.getParameter("action");
	    long imageId = StringUtils.atol(request.getParameter("id"));

	    if (action != null && imageId > 0 && action.equals("updateImage")) {
	    	Image image = Datastore.getImage(imageId);
	    	if (image != null) {
	    		image.setTitle(request.getParameter("title"));
	    		image.setStatus(PublicationStatus.valueOf(request.getParameter("status")));
		    	image.setDateModified(new Date());

	    		LOG.log(Level.INFO, "Update " + image);
	    		Datastore.saveImage(image);
	    	}
	    }
	    else if (ServletFileUpload.isMultipartContent(request)) {

	    	Map<String, String> postParameters = new HashMap<>();
	    	String storagePath = null;
	        try {
	        	ServletFileUpload upload = new ServletFileUpload();

	            FileItemIterator iterator = upload.getItemIterator(request);
	            while (iterator.hasNext()) {
	            	FileItemStream item = iterator.next();

	                if (item.isFormField()) {
	                	String value = Streams.asString(item.openStream());
	                    LOG.log(Level.INFO, "Got a form field: " + item.getFieldName() + " : " + value);
	                    postParameters.put(item.getFieldName(), value);
	                    continue;
	                }

	                InputStream stream = item.openStream();
                    String sname = item.getName();
                    String sctype = item.getContentType();

                    LOG.log(Level.INFO, "Got an uploaded file name=" + sname + ", ctype=" + sctype);

        	        if (stream != null && sname != null) {
        	        	storagePath = saveImage(stream, sname, sctype);
        	        }
	            }
	        }
	        catch (IOException e) {
	        	LOG.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
	        }
	        catch (FileUploadException e) {
	        	LOG.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
	        }

	        if (storagePath != null) {
		    	Image image = new Image();
				image.setTitle(postParameters.get("title"));
				image.setStoragePath(storagePath);
				image.setStatus(PublicationStatus.valueOf(postParameters.get("status")));

		    	Date now = new Date();
				image.setDateCreated(now);
				image.setDateModified(now);
				image.setAuthorId(user.getId());
		    	LOG.log(Level.INFO, "Create " + image);
		    	Datastore.saveImage(image);
	        }
	    }


	    response.sendRedirect("/image");
	}

	private String saveImage(InputStream stream, String sname, String sctype) throws IOException {

        GcsFilename gcsfileName = new GcsFilename(bucketName, UUID.randomUUID().toString());
        GcsFileOptions options = GcsFileOptions.getDefaultInstance();

        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(gcsfileName, options);

        LOG.log(Level.INFO, "writing upload file to " + gcsfileName.getBucketName() + " :: " + gcsfileName.getObjectName());

        long l = Streams.copy(stream, Channels.newOutputStream(outputChannel), true);
        LOG.log(Level.INFO, "copied " + l + " bytes");

        return gcsfileName.getObjectName();
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
			request.setAttribute("image", Datastore.getImage(id));
		}

		request.setAttribute("publicationStatus", PublicationStatus.values());
    	request.setAttribute("images", Datastore.getImages(null, 0, 10));

		RequestDispatcher rd = request.getRequestDispatcher("/image.jsp");
		rd.forward(request, response);
	}

}
