package Search.DataManipulation;

/**
 * Created by NCollins on 10/23/2014.
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class DataValidator {
    static Logger log = Logger.getLogger(DataValidator.class.getName());

    public boolean validBundle(String bundleId) {
        String[] bundleElements = bundleId.split("\\.");

        boolean valid = true;

        for (String bundleElement : bundleElements) {
            String regex = "[a-zA-Z][0-9a-zA-Z_]*";
            boolean test = bundleElement.matches(regex);
            if (!test) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public boolean matchNameBundle(String name, String bundleId) {
        String[] bundleElements = bundleId.toLowerCase().split("\\.");
        String[] nameElements = name.toLowerCase().split("\\s+");
        int elementCount = nameElements.length;
        int matches = 0;
        boolean match = false;

        for (String bundleElement : bundleElements) {
            for (String nameElement : nameElements) {
                int distance = StringUtils.getLevenshteinDistance(bundleElement, nameElement);
                int nameLength = nameElement.length();
                int bundleLength = bundleElement.length();
                int lengthDistance = Math.abs(nameLength - bundleLength);

                if (distance <= (lengthDistance)) {
                    ++matches;
                }
            }
        }

        if (matches > (elementCount - 2)) {
            match = true;
        }

        return match;
    }

    public boolean validUrl(String url) {
        String regex = "http[s]*://[0-9a-zA-Z/.?*&^%$#@!_=-]+";
        return url.matches(regex);
    }
}
