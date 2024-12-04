package tests;

import api.OpenWeatherAPI;
import comparator.WeatherComparator;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ui.AccuWeatherUI;
import utils.Log;

public class WeatherComparisonTest {
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Weather Comparator");
        sparkReporter.config().setReportName("Weather Test Automation");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void compareWeather() throws InterruptedException {
        test = extent.createTest("Weather Comparison Test");
        String city = "New York";
        double allowedVariance = 2.0;

        Log.info("Starting weather comparison test for city: " + city);

        AccuWeatherUI ui = new AccuWeatherUI();
        OpenWeatherAPI api = new OpenWeatherAPI();
        WeatherComparator comparator = new WeatherComparator();

        try {
            Log.info("Fetching temperature from UI...");
            double tempUI = ui.getTemperature(city);
            Log.info("Temperature from UI: " + tempUI);

            Log.info("Fetching temperature from API...");
            double tempAPI = api.getTemperature(city);
            Log.info("Temperature from API: " + tempAPI);

            Log.info("Comparing temperatures...");
            comparator.compareTemperatures(tempUI, tempAPI, allowedVariance);

            test.pass("Weather comparison test passed. UI: " + tempUI + ", API: " + tempAPI);
            Log.info("Weather comparison test passed.");
        } catch (Exception e) {
            test.fail("Weather comparison test failed: " + e.getMessage());
            Log.error("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @AfterClass
    public void tearDownReport() {
        extent.flush();
    }
}