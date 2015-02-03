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
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    static Logger log = Logger.getLogger(SearchResult.class.getName());
    static DataHandler dataHandler = new DataHandler();

    public Document searchStore(String query) throws IOException {
        String url = "https://play.google.com/store/search?q=" + query + "&c=apps";
        return dataHandler.urlDownloader(url);
    }

    public List<String> parseResult(Document dom, int count) {
        Elements appsBox = dom.getElementsByClass("card-list").first().children();

        List<String> apps = new ArrayList<String>();

        int i = 1;

        for (Element card : appsBox) {
            String href = card.getElementsByTag("a").first().attr("href");
            apps.add("https://play.google.com" + href);
            if (i == count) {
                break;
            }
            i = ++i;
        }

        return apps;
    }
}
