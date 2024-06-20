package es.codeurjc.ais.nitflex.e2e.selenium;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    public void setup() {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    @AfterEach
    public void teardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    private WebDriver createWebDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                return new ChromeDriver(chromeOptions);
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                return new FirefoxDriver(firefoxOptions);
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                return new EdgeDriver(edgeOptions);
            case "safari":
                return new SafariDriver();  // Safari doesn't support headless mode yet
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox", "edge", "safari"})
    @DisplayName("Añadir una nueva película y comprobar que se ha creado")
    public void createFilmTest(String browser) throws Exception {
        driver = createWebDriver(browser);
        setup();
        // GIVEN: Partiendo de que estamos en la página principal de la web
        this.driver.get("http://localhost:" + this.port + "/");
        // WHEN: Creamos un nueva película
        String title = "Spider-Man: No Way Home";
        String synopsis = "Peter Parker es desenmascarado y por tanto no es capaz de separar su vida normal de los enormes riesgos que conlleva ser un súper héroe.";
        String url = "https://www.themoviedb.org/t/p/w220_and_h330_face/osYbtvqjMUhEXgkuFJOsRYVpq6N.jpg";
        String year = "2021";
        // Hacemos click en "New film"
        driver.findElement(By.xpath("//*[text()='New film']")).click();
        // Rellenamos el formulario
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("url")).sendKeys(url);
        driver.findElement(By.name("releaseYear")).sendKeys(year);
        driver.findElement(By.name("synopsis")).sendKeys(synopsis);
        // Enviamos el formulario
        driver.findElement(By.id("Save")).click();
        // THEN: Esperamos que la película creada aparezca en la nueva página resultante
        this.wait.until(ExpectedConditions.textToBe(By.id("film-title"), title));
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox", "edge", "safari"})
    public void testGuardar(String browser) {
        driver = createWebDriver(browser);
        setup();

        driver.get("http://localhost:" + this.port + "/"); // Accedemos a la pagina web de nuestra aplicación
        driver.findElement(By.id("create-film")).click(); //Localizamos y clicamos new film

        WebElement titulo = driver.findElement(By.name("title")); //Recoge en la variable titulo el contenido del elemento titulo
        titulo.sendKeys("La Vida De Pi");
        driver.findElement(By.name("releaseYear")).sendKeys("2012"); //Localiza y rellena el elemento con nombre releaseYear
        driver.findElement(By.name("url")).sendKeys("https://es.web.img3.acsta.net/medias/nmedia/18/91/30/40/20328542.jpg");//Localiza y rellena el elemento con nombre url
        driver.findElement(By.name("synopsis")).sendKeys("Tras un naufragio, Pi, hijo de un guarda de zoo, se encuentra en un bote salvavidas con un único superviviente, un tigre de bengala.");//Localiza y rellena el elemento con nombre synopsis
        driver.findElement(By.id("Save")).click();//Localizamos y clicamos save

        WebElement tituloGuardado = driver.findElement(By.id("film-title"));
        assertEquals("La Vida De Pi", tituloGuardado.getText());

        driver.findElement(By.id("all-films")).click();
        assertNotNull(driver.findElement(By.partialLinkText("La Vida De Pi")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox", "edge", "safari"})
    public void testBorrar(String browser) {
        driver = createWebDriver(browser);
        setup();

        driver.get("http://localhost:" + this.port + "/"); // Accedemos a la pagina web de nuestra aplicación
        //Creamos una pelicula y volvemos a all films

        driver.findElement(By.id("create-film")).click(); //Localizamos y clicamos new film
        driver.findElement(By.name("title")).sendKeys("Interestelar"); //Recoge en la variable titulo el contenido del elemento titulo
        driver.findElement(By.name("releaseYear")).sendKeys("2014"); //Localiza y rellena el elemento con nombre releaseYear
        driver.findElement(By.name("url")).sendKeys("https://m.media-amazon.com/images/S/pv-target-images/79194981293eabf6620ece96eb5a9c1fffa04d3374ae12986e0748800b37b9cf.jpg");//Localiza y rellena el elemento con nombre url
        driver.findElement(By.name("synopsis")).sendKeys("Un grupo de científicos y exploradores, encabezados por Cooper, se embarcan en un viaje espacial para encontrar un lugar con las condiciones necesarias para reemplazar a la Tierra y comenzar una nueva vida allí");//Localiza y rellena el elemento con nombre synopsis
        driver.findElement(By.id("Save")).click();//Localizamos y clicamos save
        driver.findElement(By.id("all-films")).click();

        //Borramos la película
        driver.findElement(By.partialLinkText("Interestelar")).click();
        driver.findElement(By.id("remove-film")).click();
        assertEquals("Film 'Interestelar' deleted", driver.findElement(By.id("message")).getText());
        driver.findElement(By.id("all-films")).click();
        List<WebElement> peliculaBorrada = driver.findElements(By.partialLinkText("Interestelar"));
        assertTrue(peliculaBorrada.isEmpty());
    }
}