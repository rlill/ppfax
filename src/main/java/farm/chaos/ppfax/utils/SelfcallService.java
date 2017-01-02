package farm.chaos.ppfax.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import farm.chaos.ppfax.persistance.Datastore;

public class SelfcallService {

	private static final Logger LOG = Logger.getLogger(SelfcallService.class.getName());

	public static String call(URL url) {

		// HTTP call
		LOG.log(Level.INFO, "URL " + url);

        BufferedReader reader = null;
		StringBuilder html = new StringBuilder();
        try {
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setConnectTimeout(15000);
			urlConn.setReadTimeout(60000);
			urlConn.setAllowUserInteraction(false);
			urlConn.setDoOutput(true);

			urlConn.setRequestProperty("User-agent", "ichbins");
			urlConn.setRequestProperty("Accept-Charset", "UTF-8");
			urlConn.setRequestProperty("Access-token", Datastore.getSecretToken());

			reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

			int httpStatus = urlConn.getResponseCode();
			String httpMessage = urlConn.getResponseMessage();

			LOG.log(Level.INFO, "Response " + httpStatus + " - " + httpMessage);

			String line;
			while ((line = reader.readLine()) != null) {
				html.append(line);
			}
		}
		catch (IOException e) {
			LOG.log(Level.WARNING, e.getClass().getSimpleName() + ": " + e.getMessage(), e);
			return null;
		}
		finally {
			if (reader != null) try { reader.close(); } catch (IOException e) { }
		}
		LOG.log(Level.INFO, "HTML " + ((html.length() > 100) ? html.substring(0, 100) : html.toString()));

		return html.toString();
	}

}
