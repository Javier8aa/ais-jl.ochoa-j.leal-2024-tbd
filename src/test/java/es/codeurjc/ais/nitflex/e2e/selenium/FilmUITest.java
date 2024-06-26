package es.codeurjc.ais.nitflex.e2e.selenium;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.codeurjc.ais.nitflex.Application;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmUITest {

    @LocalServerPort
    int port;
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setupClass() {
        String browser = System.getProperty("browser");

        if (browser == null) {
            throw new IllegalArgumentException("BROWSER environment variable not set");
        }

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                driver = new EdgeDriver(edgeOptions);
                break;
            case "safari":
                driver = new SafariDriver();
                this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    @AfterEach
    public void teardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    @DisplayName("Añadir una nueva película y comprobar que se ha creado")
    public void createFilmTest() throws Exception {
        // GIVEN
        this.driver.get("http://localhost:" + this.port + "/");

        // WHEN
        String title = "Spider-Man: No Way Home";
        String synopsis = "Peter Parker es desenmascarado y por tanto no es capaz de separar su vida normal de los enormes riesgos que conlleva ser un súper héroe.";
        String url = "https://www.themoviedb.org/t/p/w220_and_h330_face/osYbtvqjMUhEXgkuFJOsRYVpq6N.jpg";
        String year = "2021";

        WebElement newFilmButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='New film']")));
        newFilmButton.click();

        WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        titleInput.sendKeys(title);

        WebElement urlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
        urlInput.sendKeys(url);

        WebElement releaseYearInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("releaseYear")));
        releaseYearInput.sendKeys(year);

        WebElement synopsisInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("synopsis")));
        synopsisInput.sendKeys(synopsis);

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Save")));
        saveButton.click();

        WebElement filmTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("film-title")));
        assertEquals(title, filmTitle.getText().trim().replaceAll("\\s+", " "));
    }

    /*@Test
    @DisplayName("Añadir una nueva película y comprobar que se ha creado")
    public void createFilmTest() throws Exception {
        // GIVEN
        this.driver.get("http://localhost:" + this.port + "/");

        // WHEN
        String title = "Spider-Man: No Way Home";
        String synopsis = "Peter Parker es desenmascarado y por tanto no es capaz de separar su vida normal de los enormes riesgos que conlleva ser un súper héroe.";
        String url = "https://www.themoviedb.org/t/p/w220_and_h330_face/osYbtvqjMUhEXgkuFJOsRYVpq6N.jpg";
        String year = "2021";

        driver.findElement(By.xpath("//*[text()='New film']")).click();

        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("url")).sendKeys(url);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("synopsis")).sendKeys(synopsis);

        driver.findElement(By.id("Save")).click();

        this.wait.until(ExpectedConditions.textToBe(By.id("film-title"), title));
        //Prueba
    }*/

    @Test
    public void testGuardar() {
        driver.get("http://localhost:" + this.port + "/"); // Accedemos a la pagina web de nuestra aplicación

        // Wait for the 'create-film' button to be clickable and click it
        WebElement createFilmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create-film")));
        createFilmButton.click();

        // Wait for the form fields to be visible and interact with them
        WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        titulo.sendKeys("La Vida De Pi");

        WebElement releaseYear = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("releaseYear")));
        releaseYear.sendKeys("2012");

        WebElement url = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
        url.sendKeys("https://es.web.img3.acsta.net/medias/nmedia/18/91/30/40/20328542.jpg");

        WebElement synopsis = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("synopsis")));
        synopsis.sendKeys("Tras un naufragio, Pi, hijo de un guarda de zoo, se encuentra en un bote salvavidas con un único superviviente, un tigre de bengala.");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Save")));
        saveButton.click();

        // Wait for the saved film title to be visible and assert its text
        WebElement tituloGuardado = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("film-title")));
        String actualTitle = tituloGuardado.getText().trim().replaceAll("\\s+", " ");
        assertEquals("La Vida De Pi", actualTitle);

        // Wait for the 'all-films' button to be clickable and click it
        WebElement allFilmsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("all-films")));
        allFilmsButton.click();

        // Assert that the saved film is present in the film list
        WebElement filmLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("La Vida De Pi")));
        assertNotNull(filmLink);
    }

    @Test
    public void testBorrar() {
        driver.get("http://localhost:" + this.port + "/");

        // Wait for the 'create-film' button to be clickable and click it
        WebElement createFilmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create-film")));
        createFilmButton.click();

        // Wait for the form fields to be visible and interact with them
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        title.sendKeys("Interestelar");

        WebElement releaseYear = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("releaseYear")));
        releaseYear.sendKeys("2014");

        WebElement url = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
        url.sendKeys("https://m.media-amazon.com/images/S/pv-target-images/79194981293eabf6620ece96eb5a9c1fffa04d3374ae12986e0748800b37b9cf.jpg");

        WebElement synopsis = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("synopsis")));
        synopsis.sendKeys("Un grupo de científicos y exploradores, encabezados por Cooper, se embarcan en un viaje espacial para encontrar un lugar con las condiciones necesarias para reemplazar a la Tierra y comenzar una nueva vida allí.");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Save")));
        saveButton.click();

        // Wait for the 'all-films' button to be clickable and click it
        WebElement allFilmsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("all-films")));
        allFilmsButton.click();

        // Wait for the created film link to be clickable and click it
        WebElement filmLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Interestelar")));
        filmLink.click();

        // Wait for the 'remove-film' button to be clickable and click it
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("remove-film")));
        removeButton.click();

        // Wait for the confirmation message to be visible and assert its text
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualMessage = message.getText().trim().replaceAll("\\s+", " ");
        assertEquals("Film 'Interestelar' deleted", actualMessage);

        // Wait for the 'all-films' button to be clickable and click it
        WebElement allFilmsButtonAfterDelete = wait.until(ExpectedConditions.elementToBeClickable(By.id("all-films")));
        allFilmsButtonAfterDelete.click();

        // Wait for the absence of the deleted film link and assert it is not present
        boolean filmStillExists = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Interestelar")));
        assertTrue(filmStillExists);
    }

    @Test
    public void editarTest() {
        driver.get("http://localhost:" + this.port + "/"); // Accedemos a la página web de nuestra aplicación

        // Wait for the 'create-film' button to be clickable and click it
        WebElement createFilmButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("create-film")));
        createFilmButton.click();

        // Wait for the form fields to be visible and interact with them
        WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        titulo.sendKeys("Torrente");

        WebElement releaseYear = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("releaseYear")));
        releaseYear.sendKeys("2014");

        WebElement url = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("url")));
        url.sendKeys("https://as01.epimg.net/deporteyvida/imagenes/2017/10/17/portada/1508244075_414775_1508244230_noticia_normal.jpg");

        WebElement synopsis = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("synopsis")));
        synopsis.sendKeys("Simplemente TORRENTE");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Save")));
        saveButton.click();

        // Wait for the 'all-films' button to be clickable and click it
        WebElement allFilmsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("all-films")));
        allFilmsButton.click();

        // Wait for the film link to be visible and click it to edit
        WebElement filmLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Torrente")));
        filmLink.click();

        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-film")));
        editButton.click();

        // Click the cancel button to check the bug
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"new-book\"]/form/p/button")));
        cancelButton.click();

        // Wait for the 'all-films' button to be visible again after canceling
        WebElement allFilmsButtonAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("all-films")));
        assertNotNull(allFilmsButtonAfterCancel);
    }

}