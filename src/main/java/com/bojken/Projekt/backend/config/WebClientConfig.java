package com.bojken.Projekt.backend.config;

@Component
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder () {

        // Configuring the client to ignore self-signed certificates, such as mykeystore.p12, enabling https calls
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE));
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });

        return WebClient.builder()
                //.baseUrl("https://api.themoviedb.org/3/")
                .clientConnector(new ReactorClientHttpConnector(httpClient)
                        // ).filter(this.csrfTokenFilter()
                );
    }

    /*
    private ExchangeFilterFunction csrfTokenFilter() {
        return (clientRequest, next) -> {
            WebClient csrfWebClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create().secure(sslContextSpec -> {
                                try {
                                    sslContextSpec.sslContext(SslContextBuilder.forClient()
                                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                            .build());
                                } catch (SSLException e) {
                                    throw new RuntimeException(e);
                                }
                            })))
                    .build();

            return csrfWebClient.post()
                    .uri("https://localhost:8443/csrf")
                    .body(BodyInserters.fromFormData("username", "test")
                            .with("password", "test")) // Assuming you have a `/csrf` endpoint
                    .retrieve()
                    .bodyToMono(CsrfToken.class)
                    .flatMap(csrfToken -> {
                        ClientRequest requestWithCsrf = ClientRequest.from(clientRequest)
                                .header("X-CSRF-TOKEN", csrfToken.getToken())
                                .build();
                        return next.exchange(requestWithCsrf);
                    });
        };
    }
*/


    /*private WebClient createWebClientWithCsrf (WebClient.Builder builder) {

       return builder.filter(addCsrfToken())
                .build();
    }

    private ExchangeFilterFunction addCsrfToken() {

        return (request, next) -> {
            CsrfToken csrfToken = (CsrfToken) SecurityContextHolder.getContext().getAuthentication().getCredentials();

            if (csrfToken != null) {
                ClientRequest newRequest = ClientRequest.from(request)
                        .header(csrfToken.getHeaderName(), csrfToken.getToken())
                        .build();
                return next.exchange(newRequest);
            }

            return next.exchange(request);

        };

    }*/

}
