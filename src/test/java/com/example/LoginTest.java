package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

// import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {
    // Provide test data CSV file path. As below path based on Mac machine. So, lets
    // say you are using windows machine then write the below path accordingly.
    String CSV_PATH = "src/test/java/com/example/TestData.csv";
    WebDriver driver;
    private CSVReader csvReader;
    String[] csvCell;
    WebDriverWait wait;

    @BeforeTest
    public void setup() throws Exception {
        // You can specify the hardcoded value of a chrome driver or driver based on
        // your browser like below line
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\LENOVO\\Downloads\\chromedriver-win64\\chromedriver.exe");
        // OR
        // Use below line to manage driver by WebdriverManager for chrome browser in our
        // case
        // WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws CsvValidationException, IOException {
        csvReader = new CSVReader(new FileReader(CSV_PATH));
        while ((csvCell = csvReader.readNext()) != null) {
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-input.oxd-input--active")));

            WebElement usernameField = driver.findElement(By.name("username"));
            WebElement passwordField = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(
                    By.cssSelector(".oxd-button.oxd-button--medium.oxd-button--main.orangehrm-login-button"));

            try {
                evaluate(usernameField, passwordField, loginButton, csvCell[0], csvCell[1], csvCell[2]);
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-input.oxd-input--active")));
                evaluate(usernameField, passwordField, loginButton, csvCell[0], csvCell[1], csvCell[2]);

            }
        }
    }

    public void evaluate(WebElement u, WebElement p, WebElement lb, String username, String password,
            String expectedUrl) {
        u.sendKeys(username);
        p.sendKeys(password);
        lb.click();

        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(expectedUrl, actualUrl);
    }

    @AfterTest
    public void exit() {
        driver.close();
        driver.quit();
    }
}
