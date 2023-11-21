package com.example.hab;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.time.Duration;

public class PatientTest {
    final String FINAL_PATH = "C:\\Users\\radman\\Desktop\\Coventry University\\7032CEM_Secure_Design_Development\\chromedriver-win64\\ChromeDriver.exe";
    final String FINAL_EMAIL = "TESTPATIENT@TEST.COM";
    final String FINAL_PASSWORD= "TEST";

    @Test
    @DisplayName("Patient Registration")
    @Order(1)
    public void testPatientRegister() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", FINAL_PATH );

        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://localhost:6060/index.html");

        WebElement button = webDriver.findElement(By.id("regPa"));

        WebDriverWait wait = new WebDriverWait(webDriver,Duration.ofSeconds(50000));
        wait.until(ExpectedConditions.visibilityOf(button));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();

        WebElement inputFName = webDriver.findElement(By.id("firstName"));
        inputFName.sendKeys("Test");
        wait.until(ExpectedConditions.visibilityOf(inputFName));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement inputLName = webDriver.findElement(By.id("lastName"));
        inputLName.sendKeys("Test");
        wait.until(ExpectedConditions.visibilityOf(inputLName));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement email = webDriver.findElement(By.id("email"));
        email.sendKeys(FINAL_EMAIL);
        wait.until(ExpectedConditions.visibilityOf(email));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement password = webDriver.findElement(By.id("password"));
        password.sendKeys(FINAL_PASSWORD);
        wait.until(ExpectedConditions.visibilityOf(password));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement rPassword = webDriver.findElement(By.id("rPassword"));
        rPassword.sendKeys(FINAL_PASSWORD);
        wait.until(ExpectedConditions.visibilityOf(rPassword));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement allergies = webDriver.findElement(By.id("allergies"));
        allergies.sendKeys("TEST");
        wait.until(ExpectedConditions.visibilityOf(allergies));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement diseases = webDriver.findElement(By.id("diseases"));
        diseases.sendKeys("TEST");
        wait.until(ExpectedConditions.visibilityOf(diseases));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement checkbox = webDriver.findElement(By.id("consent"));
        wait.until(ExpectedConditions.visibilityOf(checkbox));
        checkbox.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        button = webDriver.findElement(By.cssSelector("button[type='submit']"));
        wait.until(ExpectedConditions.visibilityOf(button));
        button.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Alert alert = webDriver.switchTo().alert();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        alert.accept();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        webDriver.quit();
    }

    @Test
    @DisplayName("Patient Sign In")
    @Order(2)
    public void testPatientSignIn() throws InterruptedException {

        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://localhost:6060/index.html");

        WebElement button = webDriver.findElement(By.id("signInPa"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        button.click();

        WebElement email = webDriver.findElement(By.id("email"));
        email.sendKeys(FINAL_EMAIL);

        WebElement password = webDriver.findElement(By.id("password"));
        password.sendKeys(FINAL_PASSWORD);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        button = webDriver.findElement(By.cssSelector("button[type='submit']"));
        button.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webDriver.quit();

    }


}
