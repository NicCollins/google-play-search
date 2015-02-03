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

/**
 * Created by NCollins on 10/13/2014.
 */

import exceptions.MarketException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import Search.Market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class Console {
    static Logger log = Logger.getLogger(Console.class.getName());
    static String path = "src/config/log4j.properties";
    static Market market = new Market();

    public static void main(String[] args) throws IOException, InterruptedException {
        PropertyConfigurator.configure(path);
        log.trace("Initializing the BufferReader and search objects.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int run = 0;

        System.out.println("Android Market Integration Test\n");
        while (run == 0) {
            System.out.println("Please select from one of the following options:");
            System.out.println("1)Search App Store");
            System.out.println("2)Get App Data by BundleID");
            System.out.print("Enter your choice or q to exit: ");
            String choice = br.readLine();

            if (choice.equals("1")) {
                System.out.print("Enter Search query: ");

                String query = br.readLine();

                List<Map<String, String>> apps = market.search(query, 10);

                if (apps.isEmpty()){
                    System.out.println("The system was unable to reach Google at this time.");
                    System.out.print("Press Enter to Return to menu");
                    br.readLine();
                    continue;
                }

                for (Map<String, String> app : apps) {
                    if (app.size() == 0 || app.get("bundleId").isEmpty()) {
                        log.fatal("BundleID is empty, an error has occurred or thread has not completed.");
                        try {
                            throw new MarketException("BundleID is empty, an error has occurred or thread has not completed.");
                        } catch (MarketException e) {
                            log.fatal("Stack Trace: ", e);
                        }
                    }
                }

                System.out.println("The results of the query are: ");

                for (Map<String, String> app : apps) {
                    System.out.println("\tApp Result:");
                    System.out.println("\t\tApp Name: " + app.get("appName"));
                    System.out.println("\t\tBundleId: " + app.get("bundleId"));
                    System.out.println("\t\tApp Rating: " + app.get("rating"));
                    System.out.println("\t\tApp Price: " + app.get("price"));
                    System.out.println();
                }

                System.out.print("Press Enter to Return to menu");
                br.readLine();
            } else if (choice.equals("2")) {
                System.out.print("Enter BundleId: ");

                String bundleIdIn = br.readLine();

                Map<String, String> app = market.appSearch(bundleIdIn);

                System.out.println("The results of the query are: ");

                System.out.println("\tApp Result:");
                System.out.println("\t\tApp Name: " + app.get("appName"));
                System.out.println("\t\tBundleId: " + app.get("bundleId"));
                System.out.println("\t\tApp Rating: " + app.get("rating"));
                System.out.println("\t\tApp Price: " + app.get("price"));
                System.out.println();

                System.out.print("Press Enter to Return to menu");
                br.readLine();
            } else if (choice.equals("q")) {
                run = 1;
            } else {
                System.out.println("Invalid option");
            }
        }

    }

}
