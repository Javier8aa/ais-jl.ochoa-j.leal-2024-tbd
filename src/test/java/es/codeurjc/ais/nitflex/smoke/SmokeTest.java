package es.codeurjc.ais.nitflex.smoke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SmokeTest {
    @LocalServerPort
    int port;
    protected WebDriver driver;
    protected WebDriverWait wait;

    String host = System.getProperty("host");

    @BeforeEach
    public void setupClass() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    public void testWelcomeMessage() {
        driver.get("http://"+host+":"+port+"/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div[1]/h1")));
        assertEquals("Welcome to Nitflex", driver.findElement(By.xpath("/html/body/div/div/div/div[1]/h1")).getText());
    }
}
