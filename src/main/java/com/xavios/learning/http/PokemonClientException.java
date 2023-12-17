package com.xavios.learning.http;

public class PokemonClientException extends RuntimeException {

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public PokemonClientException(String httpStatusCode, String responseBody) {
        super(getMessage(httpStatusCode, responseBody));
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }


    private static String getMessage(String httpStatusCode, String responseBody) {
        return httpStatusCode.concat("-").concat(responseBody);
    }

    private final String httpStatusCode;
    private final String responseBody;
}
