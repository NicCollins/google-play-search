/**
 * Created by NCollins on 12/1/2014.
 */

import Search.AppDataTest;
import Search.DataManipulation.DataHandlerTest;
import Search.DataManipulation.DataParserTest;
import Search.DataManipulation.DataValidatorTest;
import Search.MarketTest;
import Search.SearchResultTest;
import Search.ThreadedSearchTest;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({AppDataTest.class, MarketTest.class,
        SearchResultTest.class, ThreadedSearchTest.class,
        DataHandlerTest.class, DataParserTest.class,
        DataValidatorTest.class})
public class MethodTests {
}
