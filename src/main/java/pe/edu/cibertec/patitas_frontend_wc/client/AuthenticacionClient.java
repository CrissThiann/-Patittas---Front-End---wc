package pe.edu.cibertec.patitas_frontend_wc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;

@FeignClient(name = "autenticacion", url = "http://localhost:8090/autenticacion")
public interface AuthenticacionClient {
    @PostMapping("/logout")
    ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutResponseDTO);
}
