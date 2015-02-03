package Search;

/**
 * Created by ncollins on 10/10/2014.
 */

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class Market {
    static Logger log = Logger.getLogger(Market.class.getName());
    static AppData appData = new AppData();
    static SearchResult searchResult = new SearchResult();

    public List<Map<String, String>> search(String query, int count) throws IOException, InterruptedException {
        long startTime = System.nanoTime();
        List<ThreadedSearch> threads = new ArrayList<>();
        List<Map<String, String>> apps = new ArrayList<>();

        Document queryDom = searchResult.searchStore(query);

        if (queryDom == null){
            return apps;
        }

        List<String> appsList = searchResult.parseResult(queryDom, count);

        int threadCount = Thread.activeCount();

        for (String appUrl : appsList) {
            while (Thread.activeCount() > 5) {
                Thread.sleep(25);
            }

            ThreadedSearch threadedSearch = new ThreadedSearch(appUrl);
            threadedSearch.start();
            threads.add(threadedSearch);
        }

        for (ThreadedSearch thread : threads) {
            log.debug(String.format("There are %d active threads.", Thread.activeCount()));
            while (thread.isAlive) {
                Thread.sleep(10);
            }
            apps.add(thread.app);

        }

        while (Thread.activeCount() > threadCount){
            log.info(String.format("Waiting on %d active threads to close.", Thread.activeCount() - threadCount));
            Thread.sleep(50);
        }

        log.info("Threads closed successful, returning Apps information.");
        long endTime = System.nanoTime();
        long duration = (endTime -startTime)/1000000;
        log.debug(String.format("Execution time: %d milliseconds", duration));
        if (duration > 10000) {
            log.warn("The current query would have timed out.");
        }
        return apps;
    }

    public Map<String, String> appSearch (String bundleId) throws IOException {
        Document dom = appData.getAppById(bundleId);

        return appData.parseMetaData(dom);
    }
}
