package Search.DataManipulation;

/**
 * Created by NCollins on 10/10/2014.
 */

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class DataHandler {
    static Logger log = Logger.getLogger(DataHandler.class.getName());
    static DataValidator dataValidator = new DataValidator();

    public byte[] imageDownloader(String url) throws IOException {
        boolean valid = dataValidator.validUrl(url);

        if (valid) {
            byte[] image = null;
            int i = 0;
            while (i < 3) {
                try {
                    image = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
                    if (image != null) {
                        return image;
                    }
                } catch (IOException e) {
                    log.warn(String.format("Unable to get the URL: %s", url));
                    log.warn("Stack Trace: ", e);
                    i++;
                    log.warn(String.format("Attempt %d failed, retrying GET.", i));
                }
            }
            log.error(String.format("Unable to GET url: %s", url));
            return image;
        } else {
            return new byte[0];
        }
    }

    public Document urlDownloader(String url) {
        boolean valid = dataValidator.validUrl(url);

        Document dom = null;

        if (valid) {
            for (int i = 0; i < 3; i++) {
                try {
                    dom = Jsoup.connect(url).get();
                    if (dom != null) {
                        break;
                    }
                } catch (IOException e) {
                    log.fatal(String.format("Unable to get the URL: %s", url));
                    log.fatal("Stack Trace: ", e);
                }
            }
        }

        return dom;
    }
}
