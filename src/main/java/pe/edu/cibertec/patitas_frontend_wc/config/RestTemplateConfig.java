package pe.edu.cibertec.patitas_frontend_wc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplateAutenticacion(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8090/autenticacion")
                .setConnectTimeout(Duration.ofSeconds(1)) // tiempo de espera maximo para la espeade conexión con el servidor
                .setReadTimeout(Duration.ofSeconds(15)) // tiempo de espera maximo para recibir la respuesta total
                .build();

    }

    @Bean
    public RestTemplate restTemplateFinanzas(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8091/finanzas")
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Bean
    public RestTemplate restTemplateReporteria(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8092/reporteria")
                .setReadTimeout(Duration.ofSeconds(60))
                .build();
    }
}
