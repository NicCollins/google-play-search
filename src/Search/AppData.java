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

package Search;

/**
 * Created by NCollins on 10/10/2014.
 */

import Search.DataManipulation.DataHandler;
import Search.DataManipulation.DataParser;
import Search.DataManipulation.DataValidator;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.*;


public class AppData {
    static Logger log = Logger.getLogger(AppData.class.getName());
    static DataHandler dataHandler = new DataHandler();
    static DataValidator dataValidator = new DataValidator();
    static DataParser dataParser = new DataParser();

    public Document getAppById(String bundleId) {
        String url = "https://play.google.com/store/apps/details?id=" + bundleId;
        return dataHandler.urlDownloader(url);
    }

    public Map<String, String> parseMetaData(Document dom) throws IOException {
        Map<String, String> appData = new HashMap<String, String>();

        String icon = dataParser.getIcon(dom);
        appData.put("icon", icon);

        String bundleId = dataParser.getBundleId(dom);
        boolean valid = dataValidator.validBundle(bundleId);
        if (valid) {
            appData.put("bundleId", bundleId);
        } else {
            log.fatal(String.format("App BundleId: %s is not valid, this app will not be listed",bundleId));
            return new HashMap<String, String>();
        }

        String appName = dataParser.getName(dom);
        boolean match = dataValidator.matchNameBundle(appName, bundleId);
        if (match) {
            log.debug(String.format("App Name: %s appears to match App BundleId: %s", appName, bundleId));
        } else {
            log.warn(String.format("App Name: %s and App BundleId: %s do not seem to match", appName, bundleId));
        }
        appData.put("appName", appName);

        String description = dataParser.getDescription(dom);
        appData.put("description", description);

        String price = dataParser.getPrice(dom);
        appData.put("price", price);

        String category = dataParser.getCategory(dom);
        appData.put("category", category);

        String imageJson = dataParser.getThumbnails(dom);
        appData.put("images", imageJson);

        Map<String, String> metaData = dataParser.getMetaData(dom);
        for (Map.Entry<String, String> entry : metaData.entrySet() ){
            appData.put(entry.getKey(), entry.getValue());
        }

        Map<String, String> ratingData = dataParser.getRatingData(dom);
        for (Map.Entry<String, String> entry : ratingData.entrySet() ){
            appData.put(entry.getKey(), entry.getValue());
        }

        return appData;
    }
}
