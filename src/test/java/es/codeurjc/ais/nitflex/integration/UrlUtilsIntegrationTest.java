package es.codeurjc.ais.nitflex.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.ais.nitflex.utils.UrlUtils;

@DisplayName("Url Utils integration tests")
public class UrlUtilsIntegrationTest {

    private UrlUtils url;
    private String uIncorrectaF;
    private String uIncorrectaNE;
    UrlUtils urlUtils = new UrlUtils();

    @BeforeEach
    public void setUp(){
        url = new UrlUtils();
        uIncorrectaF = "esto-no-es-una-url";
        uIncorrectaNE = "https://www.urjc.es/images.jpg";
    }

    @Test
    @DisplayName("Cuando una URL NO tiene el formato correcto, debemos dar la URL por inválida")
    public void testNotValidUrl_MalformedURL(){
        String invalidUrl = "NOT_URL";
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, ()->{
            urlUtils.checkValidImageURL(invalidUrl);
        });
        assertThat(ex.getMessage())
                .contains("The url format is not valid")
                .contains("400 BAD_REQUEST");

    }

    @Test
    public void testUrlIncorrectaFormato() {
        ResponseStatusException eF = assertThrows(ResponseStatusException.class, () -> {
            url.checkValidImageURL(uIncorrectaF);
        });
        assertEquals("400 BAD_REQUEST \"The url format is not valid\"", eF.getMessage());
    }

    @Test
    public void testUrlCorrectaNoExiste() {
        ResponseStatusException eNE = assertThrows(ResponseStatusException.class, () -> {
            url.checkValidImageURL(uIncorrectaNE);
        });
        assertEquals("400 BAD_REQUEST \"Url resource does not exists\"", eNE.getMessage());
    }
}
