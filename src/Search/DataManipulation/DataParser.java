package Search.DataManipulation;

/**
 * Created by NCollins on 11/25/2014.
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class DataParser {
    static Logger log = Logger.getLogger(DataParser.class.getName());
    static DataHandler dataHandler = new DataHandler();

    public String getIcon(Document dom) throws IOException {
        Elements iconClass = dom.getElementsByClass("cover-container");
        Elements iconClass1 = iconClass.select("img.cover-image[alt=Cover art]");
        String iconUrl = iconClass1.first().attr("src");
        byte[] iconByte = dataHandler.imageDownloader(iconUrl);

        if (iconByte.length == 0) {
            log.warn("Invalid Icon url found by Search.DataManipulation.DataValidator, not adding to appData");
            return null;
        } else {
            String icon = Base64.getEncoder().encodeToString(iconByte);
            return icon;
        }
    }

    public String getName(Document dom){
        Elements appNameClass = dom.getElementsByClass("document-title");
        return appNameClass.first().child(0).ownText();
    }

    public String getBundleId(Document dom){
        Elements bundleClass = dom.getElementsByClass("buy-button-container");
        return bundleClass.first().attr("data-docid");
    }

    public String getDescription(Document dom){
        Elements descClass = dom.getElementsByClass("id-app-orig-desc");
        return descClass.first().ownText();
    }

    public String getPrice(Document dom){
        Elements priceClass = dom.select("button.price");
        Element priceClass1 = priceClass.first();
        Elements priceClass2 = priceClass1.getElementsByTag("span");
        String price = priceClass2.last().ownText();
        if (price.equalsIgnoreCase("install")) {
            price = "Free";
        } else {
            String[] split = StringUtils.split(price);
            price = split[0];
        }
        return price;
    }

    public String getCategory(Document dom){
        Elements categoryClass = dom.select("a.document-subtitle.category span[itemprop=genre]");
        return categoryClass.first().ownText();
    }

    public String getThumbnails(Document dom) throws IOException {
        Elements thumbnailsClass = dom.getElementsByClass("thumbnails");
        Elements thumbnails = thumbnailsClass.first().children();

        List<String> imageArray = new ArrayList<String>();

        for (Element images : thumbnails) {
            String imageTagUrl = images.getElementsByTag("img").first().attr("src");
            byte[] imageByte = dataHandler.imageDownloader(imageTagUrl);

            if (imageByte.length == 0) {
                continue;
            }

            String imageTag = Base64.getEncoder().encodeToString(imageByte);
            imageArray.add(imageTag);
        }

        return JSONValue.toJSONString(imageArray);
    }

    public Map<String, String> getMetaData(Document dom){
        Elements details = null;
        Map<String, String> metaData = new HashMap<>();

        Elements detailClass = dom.getElementsByClass("details-section-contents");
        for (Element testClass : detailClass) {
            if (testClass.children().first().hasClass("meta-info")) {
                details = testClass.children();
            }
        }

        assert details != null;
        for (Element detailElement : details) {
            String area = detailElement.children().first().ownText();
            String value = detailElement.children().last().ownText();

            if (!(area.equals("Permissions")||area.equals("Report")||area.equals("Developer"))) {
                metaData.put(area, value);
            }
        }
        return metaData;
    }

    public Map<String, String> getRatingData(Document dom){
        Map<String, String> ratingData = new HashMap<>();

        Elements ratingClass = dom.getElementsByClass("score-container");
        Elements ratingDom = ratingClass.first().children();

        for (Element rating : ratingDom) {
            String item = rating.attr("itemprop");
            String content = rating.attr("content");

            if (item.equals("ratingValue")) {
                ratingData.put("rating", content);
            } else if (item.equals("ratingCount")) {
                ratingData.put(item, content);
            }
        }
        return ratingData;
    }
}
