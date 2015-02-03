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
 * Created by NCollins on 10/28/2014.
 */

import Search.DataManipulation.DataHandler;
import exceptions.MarketException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadedSearch implements Runnable {
    static Logger log = Logger.getLogger(ThreadedSearch.class.getName());
    static DataHandler dataHandler = new DataHandler();
    static AppData appData = new AppData();

    private Thread t;
    private String url;
    public boolean isAlive;
    public Map<String, String> app = new HashMap<>();

    ThreadedSearch(String url) {
        this.url = url;
        isAlive = false;
    }

    @Override
    public void run() {
        this.isAlive = true;
        log.info(String.format("Start running %s", this.url));

        try {
            Document dom = dataHandler.urlDownloader(this.url);
            this.app = appData.parseMetaData(dom);
        } catch (IOException e) {
            log.fatal(String.format("Error collecting data for %s", this.url));
            log.fatal("Stack Trace: ", e);
        }

        if (app.size() == 0) {
            log.fatal(String.format("Thread has not filled in the app data for %s.", this.url));
            try {
                throw new MarketException(String.format("Thread has not filled in the app data for %s.", this.url));
            } catch (MarketException e) {
                log.fatal("Stack Trace: ", e);
            }
        } else {
            log.info(String.format("Finished running %s", this.url));
        }

        this.isAlive = false;
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, url);
            t.start();
        }
    }
}
