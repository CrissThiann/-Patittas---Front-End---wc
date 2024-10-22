package pe.edu.cibertec.patitas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc.client.AuthenticacionClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    RestTemplate restTemplateAutenticacion;

    @Autowired
    WebClient webClientAuthentication;

    @Autowired
    AuthenticacionClient autenticacionClient;


    @GetMapping("/inicio")
    public String inicio(Model model){
        LoginModel loginModel = new LoginModel("00","","");
        model.addAttribute("loginModel",loginModel);
        return "inicio";

    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        System.out.println("Consuming with Web Client...");

        // Validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {
            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

        try {

            // Invocar servicio de autenticación
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

            Mono<LoginResponseDTO> monoLoginResponseDTO = webClientAuthentication.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class);

            //recuperar resultado moodo bloquenate (Sincronico)
            LoginResponseDTO loginResponseDTO = monoLoginResponseDTO.block();

            if (loginResponseDTO.codigo().equals("00")){

                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";

            } else {

                LoginModel loginModel = new LoginModel("02", "Error: Autenticación fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";

            }

        } catch(Exception e) {

            LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema en la autenticación", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";

        }

    }

    @PostMapping("/logOut")
    public String logOut(@RequestParam("tipoDocumento") String tipoDocumento,
                         @RequestParam("numeroDocumento") String numeroDocumento,
                         Model model) {

        LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO(tipoDocumento, numeroDocumento);
        Mono<LogoutResponseDTO> monoLogoutResponseDTO = webClientAuthentication.post()
                .uri("/logout")
                .body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                .retrieve()
                .bodyToMono(LogoutResponseDTO.class);

        //recuperar resultado moodo bloquenate (Sincronico)
        LogoutResponseDTO logoutResponseDTO = monoLogoutResponseDTO.block();

        if (logoutResponseDTO.codigo().equals("00")){

            return "redirect:/login/inicio";

        } else {
            model.addAttribute("error", "Error al cerrar sesión");
            return "principal";

        }


    }

}
