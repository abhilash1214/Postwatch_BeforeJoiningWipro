package com.postwatch.testscripts;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.postwatch.base.ReadTestData;
import com.postwatch.pageobjects.LoginFacebookPage;

@Test(dataProvider="data")
public class Login extends LoginFacebookPage{
	LoginFacebookPage loginFacebookPage= new LoginFacebookPage();
	
	@Test(dataProvider = "data")
	public void loginPageValidator(Map<Object, Object> data) throws IOException {
		String seasonNameInput= (String) data.get("SeasonNameInput");
		loginFacebookPage.validateHome(seasonNameInput);
	}

	@DataProvider(name = "data")
	public Object[][] detData() throws IOException {
		Object[][] excelData = ReadTestData.readDataFromSheet("PostWatchData");
		Object[][] testCaseData = new Object[1][1];
		for (int i = 0; i < excelData.length; i++) {
			if (excelData[i][0].toString().contains("Admin Functions")) {
				testCaseData[0][0] = excelData[i][0];
			}
		}
		return testCaseData;
	}

}
