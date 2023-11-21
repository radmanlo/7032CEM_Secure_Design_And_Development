package com.example.hab;

import com.example.hab.controller.AuthController;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.service.AuthService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class HealthCenterTest {

    final String path = "C:\\Users\\radman\\Desktop\\Coventry University\\7032CEM_Secure_Design_Development\\chromedriver-win64\\ChromeDriver.exe";

    final String FINAL_EMAIL = "TESTHC@TEST.COM";
    final String FINAL_PASSWORD= "TEST";

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Test
    @DisplayName("Create Health Center Controller")
    @Order(1)
    public void testCreateResearch() {

        HealthCenterDto inputHC = new HealthCenterDto();
        HealthCenterDto outputHC = new HealthCenterDto();

        when(authService.createHC(inputHC)).thenReturn(outputHC);

        ResponseEntity<HealthCenterDto> response = authController.createHealthCenter(inputHC);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status Code Attribute");
        assertEquals(outputHC, response.getBody(), "Response Attribute");
    }

    @Test
    @DisplayName("Health Center Registration")
    @Order(2)
    public void testCreateResearchAPI() {
        System.setProperty("webdriver.chrome.driver", path);

        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://localhost:6060/index.html");
        WebElement button = webDriver.findElement(By.id("hcReg"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button.click();

        WebElement name = webDriver.findElement(By.id("name"));
        name.sendKeys("Test");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement email = webDriver.findElement(By.id("email"));
        email.sendKeys(FINAL_EMAIL);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement password = webDriver.findElement(By.id("password"));
        password.sendKeys(FINAL_PASSWORD);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement rPassword = webDriver.findElement(By.id("rPassword"));
        rPassword.sendKeys(FINAL_PASSWORD);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        button = webDriver.findElement(By.cssSelector("button[type='submit']"));
        button.click();

        try {
            Thread.sleep(2000);
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
    @DisplayName("Health Center Sign In")
    @Order(2)
    public void testPatientSignIn() throws InterruptedException {

        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://localhost:6060/index.html");

        WebElement button = webDriver.findElement(By.id("signInHc"));

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
