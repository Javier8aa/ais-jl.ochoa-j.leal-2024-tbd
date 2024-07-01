package es.codeurjc.ais.nitflex.e2e.selenium;

import es.codeurjc.ais.nitflex.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebAppTest {

    @LocalServerPort
    int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setupTest() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");
        this.driver = new EdgeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    void CreateFilmTest(){
        driver.get("http://localhost:" + this.port + "/");

        wait.until(elementToBeClickable(By.id("create-film")));
        int numOriginal = driver.findElements(By.className("film-title")).size();
        driver.findElement(By.id("create-film")).click();

        String nuevoTitulo = "Fast and furious X";
        wait.until(presenceOfElementLocated(By.name("title"))).sendKeys(nuevoTitulo);
        String nuevoAnio = "2023";
        driver.findElement(By.name("releaseYear")).sendKeys(nuevoAnio);
        String nuevaUrl = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/x3zlm6VxPvVrYWE3bHkYUQMR798.jpg";
        driver.findElement(By.name("url")).sendKeys(nuevaUrl);
        String nuevaSynopsis = "Dominic Toretto debera proteger a su equipo y a su familia de Dante Reyes. Este antagonista es el hijo del narcotraficante Hernan Reyes y busca venganza por lo sucedido en 'Fast five', cuando Toretto y su grupo les robaron mucho dinero en Rio de Janeiro.";
        driver.findElement(By.name("synopsis")).sendKeys(nuevaSynopsis);

        wait.until(elementToBeClickable(By.id("Save"))).click();
        wait.until(elementToBeClickable(By.id("all-films"))).click();

        wait.until(elementToBeClickable(By.id("create-film")));
        assertEquals(numOriginal+1, driver.findElements(By.className("film-title")).size());
    }

    @Test
    void RemoveFilmTest(){
        driver.get("http://localhost:" + this.port + "/");

        wait.until(elementToBeClickable(By.id("create-film")));
        int numOriginal = driver.findElements(By.className("film-title")).size();
        driver.findElement(By.id("create-film")).click();

        String nuevoTitulo = "Kung Fu Panda";
        wait.until(presenceOfElementLocated(By.name("title"))).sendKeys(nuevoTitulo);
        String nuevoAnio = "2018";
        driver.findElement(By.name("releaseYear")).sendKeys(nuevoAnio);
        String nuevaUrl = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/gFrK1vBfvcfN6UlVrSuD2napXVR.jpg";
        driver.findElement(By.name("url")).sendKeys(nuevaUrl);
        String nuevaSynopsis = "Po, el guerrero del dragon, luchara con sus amigos contra los peligros que les afloran";
        driver.findElement(By.name("synopsis")).sendKeys(nuevaSynopsis);

        wait.until(elementToBeClickable(By.id("Save"))).click();
        wait.until(elementToBeClickable(By.id("all-films"))).click();
        wait.until(elementToBeClickable(By.id("Kung Fu Panda"))).click();//FALLO
        wait.until(elementToBeClickable(By.id("remove-film"))).click();
        wait.until(elementToBeClickable(By.id("all-films"))).click();

        wait.until(elementToBeClickable(By.id("create-film")));
        assertEquals(numOriginal, driver.findElements(By.className("film-title")).size());
    }

    @Test
    void CancelEditTest(){
        driver.get("http://localhost:" + this.port + "/");

        wait.until(elementToBeClickable(By.id("create-film"))).click();

        String nuevoTitulo = "Vengadores: Infinity War";
        wait.until(presenceOfElementLocated(By.name("title"))).sendKeys(nuevoTitulo);
        String nuevoAnio = "2018";
        driver.findElement(By.name("releaseYear")).sendKeys(nuevoAnio);
        String nuevaUrl = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/ksBQ4oHQDdJwND8H90ay8CbMihU.jpg";
        driver.findElement(By.name("url")).sendKeys(nuevaUrl);
        String nuevaSynopsis = "Ironman y Capitan America deberan limar diferencias y buscar sus mejores aliados para vencer a Thanos";
        driver.findElement(By.name("synopsis")).sendKeys(nuevaSynopsis);

        wait.until(elementToBeClickable(By.id("Save"))).click();
        wait.until(elementToBeClickable(By.id("all-films"))).click();
        wait.until(elementToBeClickable(By.id("Vengadores: Infinity War"))).click();//AQUI
        wait.until(elementToBeClickable(By.id("edit-film"))).click();
        wait.until(elementToBeClickable(By.id("Cancel"))).click();

        wait.until(elementToBeClickable(By.id("edit-film")));
        assertEquals(nuevoTitulo, driver.findElement(By.id("film-title")).getText());
    }

    @Test
    void errorCreateInvalidYear(){
        driver.get("http://localhost:" + this.port + "/");

        wait.until(elementToBeClickable(By.id("create-film")));
        driver.findElement(By.id("create-film")).click();

        String nuevoTitulo = "Peli Inexistente";
        wait.until(presenceOfElementLocated(By.name("title"))).sendKeys(nuevoTitulo);
        String nuevoAnio = "1894";
        driver.findElement(By.name("releaseYear")).sendKeys(nuevoAnio);
        String nuevaUrl = "https://th.bing.com/th/id/R.c8092fc59c35ec31878c55f6f8cab8dc?rik=gB0jsuTkA20w8g&pid=ImgRaw&r=0.jpg";
        driver.findElement(By.name("url")).sendKeys(nuevaUrl);
        String nuevaSynopsis = "Esta película no pudo ser creada por que año es incorrecto";
        driver.findElement(By.name("synopsis")).sendKeys(nuevaSynopsis);

        wait.until(elementToBeClickable(By.id("Save"))).click();

        this.wait.until(ExpectedConditions.textToBe(By.id("message"),"The year is invalid: should be since 1895"));
    }
}