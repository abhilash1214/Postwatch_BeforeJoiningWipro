package com.postwatch.pageobjects;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.postwatch.base.ResuableLibrary;

public class LoginFacebookPage extends ResuableLibrary {

	@FindBy(xpath = "//*[@id=\"SW\"]/div[1]/div[1]/a/img")
	public static WebElement logo;

	@FindBy(xpath = "//*[@id=\"SW\"]/div[1]/div[2]/div/div/nav/ul/li[2]/a/span[2]/text()")
	public static WebElement hotelName;

	public void validateHome(String seasonNameInput) throws IOException {
		PageFactory.initElements(driver, LoginFacebookPage.class);
		try {
			logo.isDisplayed();
			System.out.println(hotelName.getText());
			System.out.println("seasonNameInput: " + seasonNameInput);
			logSuccessAndScreenshot("Navigated to Home Page");
		} catch (Exception e) {
			logFailureAndScreenshot("Navigated to Home Page");
		}
	}
}
