package com.example;

import java.time.Duration;

import org.testng.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SetupTest {
    WebDriver driver;

    @Test
    public void test() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\LENOVO\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-input.oxd-input--active")));

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver
                .findElement(By.cssSelector(".oxd-button.oxd-button--medium.oxd-button--main.orangehrm-login-button"));
        usernameField.sendKeys("Admin");
        passwordField.sendKeys("admin123");
        loginButton.click();

        String expectedUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(expectedUrl, actualUrl);
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }
}
