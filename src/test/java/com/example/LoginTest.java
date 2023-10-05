package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {
    String CSV_PATH = "src/test/java/com/example/TestData.csv";
    WebDriver driver;
    private CSVReader csvReader;
    String[] csvCell;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setupTest() throws Exception {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
    }

    @DataProvider(name = "TESTDATA")
    public Object[][] testData() throws IOException, CsvException {
        csvReader = new CSVReader(new FileReader(CSV_PATH));
        List<String[]> csvData = csvReader.readAll();
        Object[][] testData = new Object[csvData.size()][3];
        for (int i = 0; i < csvData.size(); i++) {
            testData[i] = csvData.get(i);
        }
        return testData;
    }

    @Test(dataProvider = "TESTDATA")
    public void test(String username, String password, String expectedUrl) {
        driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-input.oxd-input--active")));
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver
                .findElement(By.cssSelector(".oxd-button.oxd-button--medium.oxd-button--main.orangehrm-login-button"));

        evaluate(usernameField, passwordField, loginButton, username, password, expectedUrl);
    }

    public void evaluate(WebElement u, WebElement p, WebElement lb, String username, String password,
            String expectedUrl) {
        u.sendKeys(username);
        p.sendKeys(password);
        lb.click();

        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(expectedUrl, actualUrl);
    }

    @AfterMethod
    public void exitTest() {
        driver.quit();
    }

    @AfterClass
    public void exit() {
        driver.close();

    }
}
