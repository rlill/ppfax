package farm.chaos.ppfax.controller;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.Streams;
import org.apache.http.HttpStatus;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import farm.chaos.ppfax.model.Image;
import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.StringUtils;

@SuppressWarnings("serial")
public class ImageService extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(ImageService.class.getName());

	private final int BUFFER_SIZE = 10240;

    private final GcsService gcsService = GcsServiceFactory.createGcsService(
    		new RetryParams.Builder()
				    .initialRetryDelayMillis(10)
				    .retryMaxAttempts(10)
				    .totalRetryPeriodMillis(15000)
				    .build());

//    private String bucketName = "ppfax-cms.appspot.com"; // TODO: variable
    private String bucketName = "blubberblubb"; // TODO: variable

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		/*
		 * see http://narup.blogspot.de/2014/11/uploading-to-google-cloud-storage-from.html
		 * https://cloud.google.com/appengine/docs/java/javadoc/com/google/appengine/api/images/ImagesService
		 */

		long id = StringUtils.getIdFromUri(request.getRequestURI());

		if (id == 0) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

		Image image = Datastore.getImage(id);

        GcsFilename gcsfileName = new GcsFilename(bucketName, image.getStoragePath());

        response.setContentType("image/jpeg");

		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsfileName, 0, BUFFER_SIZE);
		Streams.copy(Channels.newInputStream(readChannel), response.getOutputStream(), true);

	}

}
