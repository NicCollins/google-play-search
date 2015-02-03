package Search.DataManipulation;

import junit.framework.TestCase;

public class DataValidatorTest extends TestCase {
    public static DataValidator dataValidator = new DataValidator();

    public void testValidBundle() throws Exception {
        String bundleId = "com.test.bundle";
        assertTrue("The bundleId validation is wrong", dataValidator.validBundle(bundleId));
    }

    public void testInvalidBundle() throws Exception {
        String bundleId = "3com.invalid.bundle";
        assertFalse("The bundleId validation is wrong", dataValidator.validBundle(bundleId));
    }

    public void testMatchNameBundle() throws Exception {

    }

    public void testValidUrl() throws Exception {

    }
}