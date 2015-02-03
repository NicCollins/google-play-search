/**
 * Created by NCollins on 12/1/2014.
 */

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args){
        System.out.println("Running Methods Tests");

        Result methodResult = JUnitCore.runClasses(MethodTests.class);
        for (Failure failure : methodResult.getFailures()){
            System.out.println(failure.toString());
        }

        System.out.println();
        System.out.println("Running Regression Tests");

        Result regressionResult = JUnitCore.runClasses(RegressionTests.class);
        for (Failure failure : regressionResult.getFailures()){
            System.out.println(failure.toString());
        }

        System.out.println();
        System.out.println("Running Performance Tests");

        Result performanceResult = JUnitCore.runClasses(PerformanceTests.class);
        for (Failure failure : performanceResult.getFailures()){
            System.out.println(failure.toString());
        }

        System.out.println();
        System.out.println("Testing Complete");
    }
}