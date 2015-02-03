/*
 * Program to fetch search results from Google Play
 * Copyright (C) 2015.  Nicolas A. Collins
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
