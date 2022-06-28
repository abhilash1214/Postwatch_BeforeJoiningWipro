package com.postwatch.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class ResuableLibrary extends Base {
	public static String timeStamp;

	public static String getBase64Screenshot() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}

	public static String getScreenshot(WebDriver driver, String className) {
		timeStamp = new SimpleDateFormat("dd-MM-YY-HH-mm-ss").format(Calendar.getInstance().getTime());
		screenshotPath = System.getProperty("user.dir") + File.separatorChar + "Results" + File.separatorChar
				+ "Screenshots" + File.separatorChar + className + timeStamp + ".jpeg";
		try {
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File destFile = new File(screenshotPath);
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			System.out.println("Exception faces while taking screenshot: " + e.getMessage());
			return screenshotPath;
		}
		return screenshotPath;
	}

	public void logInfoAndScreenshot(String info) throws IOException {
		extentTest.log(Status.INFO, MarkupHelper.createLabel(info, ExtentColor.CYAN));

		screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
		// extentTest.info("Details",
		// MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		extentTest.info("Details",
				MediaEntityBuilder.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
	}

	public void logSuccessAndScreenshot(String info) throws IOException {
		extentTest.log(Status.PASS, MarkupHelper.createLabel(info, ExtentColor.GREEN));

		screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
		// extentTest.pass("Details",
		// MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		extentTest.pass("Details",
				MediaEntityBuilder.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
	}

	public void logFailureAndScreenshot(String info) throws IOException {
		extentTest.log(Status.FAIL, MarkupHelper.createLabel(info, ExtentColor.RED));

		screenshotPath = ResuableLibrary.getScreenshot(driver, this.getClass().getName());
		// extentTest.fail("Details",
		// MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		extentTest.fail("Details",
				MediaEntityBuilder.createScreenCaptureFromBase64String(ResuableLibrary.getBase64Screenshot()).build());
	}
}