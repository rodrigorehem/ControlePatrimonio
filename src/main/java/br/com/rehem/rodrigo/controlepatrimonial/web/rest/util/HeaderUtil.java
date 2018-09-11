package br.com.rehem.rodrigo.controlepatrimonial.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-controlePatrimonialApp-alert", message);
        headers.add("X-controlePatrimonialApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("Uma nova " + entityName + " foi criada com a identificado " + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " foi atualizado com a identificado " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("A " + entityName+" de identificação "+param+" foi removido", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-controlePatrimonialApp-error", defaultMessage);
        headers.add("X-controlePatrimonialApp-params", entityName);
        return headers;
    }
}
