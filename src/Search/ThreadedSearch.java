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
