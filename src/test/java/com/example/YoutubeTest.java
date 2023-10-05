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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class YoutubeTest {
    String CSV_PATH = "src/test/java/com/example/YoutubeTestData.csv";
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
        Object[][] testData = new Object[csvData.size()][2];
        for (int i = 0; i < csvData.size(); i++) {
            testData[i] = csvData.get(i);
        }
        return testData;
    }

    @Test(dataProvider = "TESTDATA")
    public void test(String searchTerm, String expectedFirstVideoTitle) {
        driver.navigate().to("https://www.youtube.com");
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search_query")));
        WebElement searchInput = driver.findElement(By.name("search_query"));
        WebElement searchButton = driver.findElement(By.id("search-icon-legacy"));

        searchInput.sendKeys(searchTerm);
        searchButton.click();

        String actualFirstVideoTitle = driver.findElement(By.cssSelector("#video-title > .yt-formatted-string")).getText();

        Assert.assertEquals(expectedFirstVideoTitle, actualFirstVideoTitle);
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
