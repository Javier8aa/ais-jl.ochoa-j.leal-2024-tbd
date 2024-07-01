package es.codeurjc.ais.nitflex.unitary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.nitflex.film.Film;
import es.codeurjc.ais.nitflex.film.FilmRepository;
import es.codeurjc.ais.nitflex.film.FilmService;
import es.codeurjc.ais.nitflex.notification.NotificationService;
import es.codeurjc.ais.nitflex.utils.UrlUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@DisplayName("FilmService Unitary tests")
public class FilmServiceUnitaryTest {

    private Film savedFilm;
    private FilmRepository repositorioMock;
    private NotificationService notificacionMock;
    private UrlUtils urlMock;
    private FilmService filmService;

    @BeforeEach
    public void setUp(){
        // Como FilmService utiliza 3 clases, tendrá 3 dependencias, habrá que mockearlas
        repositorioMock = mock(FilmRepository.class);             //Mock de FilmRepository
        notificacionMock = mock(NotificationService.class);       //Mock NotificationService
        urlMock = mock(UrlUtils.class);                           //Mock UrlUtils
        filmService = new FilmService(repositorioMock, notificacionMock, urlMock);
    }

    @Test
    @DisplayName("Cuando se guarda una película (con una URL correcta) utilizando FilmService, se guarda en el repositorio y se lanza una notificación")
    public void createFilm() {

        repositorioMock = mock(FilmRepository.class);
        notificacionMock = mock(NotificationService.class);
        urlMock = mock(UrlUtils.class);
        filmService = new FilmService(repositorioMock, notificacionMock, urlMock);

        Film book = new Film("FAKE FILM", "FAKE DESCRIPTION", 1900, "FAKE URL");

        // Given
        when(repositorioMock.save(book)).thenReturn(book);

        // When
        filmService.save(book);

        // Then
        verify(repositorioMock, times(1)).save(book);
        verify(urlMock, times(1)).checkValidImageURL("FAKE URL");
        verify(notificacionMock).notify("Film Event: Film with title=" + book.getTitle() + " was created");
    }

    @Test
    public void testGuardarPeliculaURLCorrecta() {
        //                              GIVEN
        Film pelicula = new Film();
        pelicula.setUrl("https://www.urjc.es/images/Covers/cover_intranet_urjc.jpg");   // Creamos una película con URL correcta
        pelicula.setTitle("Prueba");

        //                              WHEN
        doNothing().when(urlMock).checkValidImageURL(pelicula.getUrl());
        when(repositorioMock.save(any(Film.class))).thenReturn(pelicula);               //Cuando se guarde cualquier pelicula, deberá devolver la película guardada
        doNothing().when(notificacionMock).notify(anyString());
        // Aserciones
        assertDoesNotThrow(() -> {                                                      //Comprobamos que se puede ejecutar sin lanzar ninguna excepción
            savedFilm = filmService.save(pelicula);
        });
        assertEquals(pelicula, savedFilm);                                              //Comparamos la pelicula con la pelicula guardada
        //                              THEN
        verify(urlMock, times(1)).checkValidImageURL(pelicula.getUrl());    // Verificamos que se llama a checkValidImageURL, se llama 2 veces, en el save y en el assetDoesNotThrow
        verify(notificacionMock, times(1)).notify("Film Event: Film with title="+pelicula.getTitle()+" was created");   //Comprobamos que se llama a notificación 1 vez, al guardar
        verify(repositorioMock,times(1)).save(pelicula);                    //Verificamos que se llama a guardar una película 1 vez
    }

    @Test
    public void testGuardarPeliculaURLErronea() {
        //                              GIVEN
        Film pelicula = new Film();
        pelicula.setUrl("esto-no-es-una-url");         // Creamos una película con URL incorrecta
        pelicula.setTitle("Prueba");

        //WHEN
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "400 BAD_REQUEST \"The url format is not valid\""))
                .when(urlMock).checkValidImageURL(pelicula.getUrl());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            filmService.save(pelicula);
        });
        assertEquals("400 BAD_REQUEST \"400 BAD_REQUEST \"The url format is not valid\"\"", ex.getMessage());
        verify(repositorioMock, never()).save(pelicula);
        verify(notificacionMock, never()).notify(anyString());
    }

    @Test
    @DisplayName("No se debe permitir crear una película con un año anterior a 1895")
    public void testGuardarPeliculaConAnoNoValido() {
        //GIVEN
        Film pelicula = new Film();
        pelicula.setTitle("Pelicula Invalida");
        pelicula.setReleaseYear(1800); // Año no válido
        pelicula.setUrl("https://www.urjc.es/images/Covers/cover_intranet_urjc.jpg");

        //WHEN
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The year is invalid: should be since 1895"))
                .when(repositorioMock).save(pelicula);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            filmService.save(pelicula);
        });

        //THEN
        assertEquals("The year is invalid: should be since 1895", ex.getMessage());
        verify(repositorioMock, never()).save(pelicula);
        verify(notificacionMock, never()).notify(anyString());
    }

}
