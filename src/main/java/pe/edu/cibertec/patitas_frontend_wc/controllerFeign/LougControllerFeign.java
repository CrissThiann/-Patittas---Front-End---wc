package pe.edu.cibertec.patitas_frontend_wc.controllerFeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_frontend_wc.client.AuthenticacionClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/logout")
@CrossOrigin("http://localhost:5173/")
public class LougControllerFeign {

    @Autowired
    AuthenticacionClient authenticacionClient;

    @PostMapping("/logout-feign")
    public ResponseEntity<LogoutResponseDTO> logOut(@RequestBody LogoutRequestDTO logoutRequestDTO){
        System.out.println("Logout Session with Feign");

        //Se realizara la llamada al servicio de autenticacion
        try {
            ResponseEntity<LogoutResponseDTO> responseEntity = Mono.just( authenticacionClient.logout(logoutRequestDTO))
                    .flatMap(response -> { // Se realiza el mapeo de la respuesta del servicio
                        // Se verifica si la respuesta es exitosa
                        if (response.getStatusCode().is2xxSuccessful()) {
                            // Se obtiene el cuerpo de la respuesta
                            LogoutResponseDTO logoutResponseDTO = response.getBody();
                            // Se verifica si la respuesta es exitosa
                            if (logoutResponseDTO != null && logoutResponseDTO.codigo().equals("00")) {
                                return Mono.just(ResponseEntity.ok(logoutResponseDTO));
                            } else {
                                return Mono.just(ResponseEntity.status(401).body(new LogoutResponseDTO("02", "Error: No se pudo cerrar la sesión.", "", "")));
                            }
                        } else {
                            return Mono.just(ResponseEntity.status(500).body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión.", "", "")));
                        }
                    }).onErrorResume(e -> { // Se captura el error en caso de que ocurra
                        // Se imprime el error en consola
                        System.out.println("Error: " + e.getMessage());
                        return Mono.just(ResponseEntity.status(500).body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión.", "", "")));
                    }).block(); // Bloquea el hilo hasta que el Mono se complete
            return responseEntity;

        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(new LogoutResponseDTO("99", "Error: Ocurrió un problema en el cierre de sesión.", "", ""));

        }

    }


}
