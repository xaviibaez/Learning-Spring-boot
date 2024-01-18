package com.xavios.learning.pokemons.http;

public class ClientException extends RuntimeException {
    //TODO -> Es runtime exception porque se ejecuta en tiempo de ejecucion?
    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public ClientException(String httpStatusCode, String responseBody) {
        super(getMessage(httpStatusCode, responseBody));
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    private static String getMessage(String httpStatusCode, String responseBody) {
        return responseBody.concat(" - ").concat(httpStatusCode);
    }

    private final String httpStatusCode;
    private final String responseBody;
}
