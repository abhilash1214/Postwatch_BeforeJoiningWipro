package com.postwatch.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Base {

	public static WebDriver driver;
	public static Properties prop;
	public static FileInputStream ip;
	public static ExtentHtmlReporter extentHtmlReporter;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;
	public static String screenshotPath;

	@BeforeSuite
	public void setBase(ITestContext ctx) throws IOException {

		String suiteName = ctx.getCurrentXmlTest().getSuite().getName();
		String timeStamp = new SimpleDateFormat("dd-MM-YY-HH-mm-ss").format(Calendar.getInstance().getTime());

		// 1. INITILIZING PROPERTY FILES
		prop = new Properties();
		ip = new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "config.property");
		prop.load(ip);

		// 3. EXTENT REPORTS INITILIZATION
		extentHtmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + File.separatorChar + "Results" + File.separatorChar + "ExtentReport"
						+ File.separatorChar + suiteName + " Report- " + timeStamp + ".html");

		extentReports = new ExtentReports();
		extentReports.attachReporter(extentHtmlReporter);

		extentReports.setSystemInfo("Application Name", prop.getProperty("application"));
		extentReports.setSystemInfo("Environment", prop.getProperty("environment"));
		extentReports.setSystemInfo("User Name", prop.getProperty("id"));

		extentHtmlReporter.config().setChartVisibilityOnOpen(true);
		extentHtmlReporter.config().setDocumentTitle("POSTWATCH AUTOMATION");
		extentHtmlReporter.config().setReportName("POSTWATCH Automation Report");
		extentHtmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		extentHtmlReporter.config().setTheme(Theme.STANDARD);

		// 2. INITILIZING Browsers
		switch (prop.getProperty("browser").toUpperCase()) {
		case "FIREFOX":
			System.setProperty("webDriver.gecko.driver", "geckodriver.exe");
			System.out.println("Executing on Firefox browser");
			driver = new FirefoxDriver();
			break;

		case "CHROME":
			System.setProperty("webDriver.chrome.driver", "chromeDriver.exe");
			System.out.println("Executing on chrome browser");
			driver = new ChromeDriver();
			break;

		case "IE":
			System.setProperty("webDriver.ie.driver", "IEDriverServer.exe");
			System.out.println("Executing on IE browser");
			driver = new InternetExplorerDriver();
			break;
		}
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	// 4. INITILIZING EXTENTTEST OBJECT
	@BeforeClass
	public void reportSetup() {
		extentTest = extentReports.createTest(this.getClass().getSimpleName());
		System.out.println("Class under execution: " + extentTest);
	}

	// 5. LAUNCHING URL BEFORE EVERY TEST
	@BeforeMethod
	public void launchURL() {
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
		extentTest.log(Status.INFO,
				MarkupHelper.createLabel("Launching URL: " + prop.getProperty("url"), ExtentColor.CYAN));
		extentTest.log(Status.INFO,
				MarkupHelper.createLabel("Browser used: " + prop.getProperty("browser"), ExtentColor.CYAN));
	}

	@AfterMethod
	public void logStatus(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(Status.INFO,
					MarkupHelper.createLabel(result.getName() + " Test Scenario Failed", ExtentColor.RED));

			screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
			// extentTest.fail("Details",
			// MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			extentTest.fail("Details", MediaEntityBuilder
					.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " Test Scenario Passed", ExtentColor.GREEN));

			screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
			extentTest.pass("Details", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			extentTest.pass("Details", MediaEntityBuilder
					.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
		}

		else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " Test Scenario Skipped", ExtentColor.GREEN));

			screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
			// extentTest.skip("Details",
			// MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			extentTest.skip("Details", MediaEntityBuilder
					.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
		}
	}

	@AfterSuite
	public void quitbrowsers() {
		driver.quit();
	}

	@AfterSuite
	public void finishReport() {
		extentReports.flush();
	}
}
