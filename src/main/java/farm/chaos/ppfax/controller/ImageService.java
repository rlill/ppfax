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

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;
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

    private final GcsService gcsService = GcsServiceFactory.createGcsService(
    		new RetryParams.Builder()
				    .initialRetryDelayMillis(10)
				    .retryMaxAttempts(10)
				    .totalRetryPeriodMillis(15000)
				    .build());

    private String bucketName = AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		long id = StringUtils.getIdFromUri(request.getRequestURI());

		if (id == 0) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

		int rno = StringUtils.getRnoFromUri(request.getRequestURI());

		if (rno == 0) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

		Image image = Datastore.getImage(id);

        GcsFilename gcsfileName = new GcsFilename(bucketName, image.getStoragePath());

        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "max-age=2628000, public");


        if (rno == 822) {
			GcsInputChannel readChannel = gcsService.openReadChannel(gcsfileName, 0);
			Streams.copy(Channels.newInputStream(readChannel), response.getOutputStream(), true);
			return;
        }

        Transform t = null;
        switch (rno) {
        case 1:
        	t = ImagesServiceFactory.makeResize(50, 50, false);
        	break;
        case 2:
        	t = ImagesServiceFactory.makeResize(200, 200, false);
        	break;
        case 3:
        	t = ImagesServiceFactory.makeResize(400, 400, false);
        	break;
        case 4:
        	t = ImagesServiceFactory.makeResize(600, 600, false);
        	break;
        case 5:
            t = ImagesServiceFactory.makeResize(800, 800, false);
            break;
        }


		if (t == null) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			RequestDispatcher rd = request.getRequestDispatcher("/notfoundpage.jsp");
			rd.forward(request, response);
			return;
		}

        com.google.appengine.api.images.Image i = ImagesServiceFactory.makeImageFromFilename("/gs/" + bucketName + "/" + image.getStoragePath());

        OutputSettings os = new OutputSettings(ImagesService.OutputEncoding.JPEG);
        os.setQuality(90);
        i = ImagesServiceFactory.getImagesService().applyTransform(t, i, os);

        response.getOutputStream().write(i.getImageData());

	}

}
