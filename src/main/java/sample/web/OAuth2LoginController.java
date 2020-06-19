package sample.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;


@Controller
public class OAuth2LoginController {

    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String index(Model model,
                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                        @AuthenticationPrincipal OAuth2User oauth2User) {
        model.addAttribute("userName", oauth2User.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        return "index";
    }

    @GetMapping("/login-oauth")
    public String loginPAge(Model model){
        Map<String, String> oauth2AuthenticationUrls
                = new HashMap<>();
        Iterable<ClientRegistration> clientRegistrations = null;
        clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(), "oauth2/authorization/"+
                        registration.getRegistrationId()));

        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "login-oauth";
    }

    @Autowired
    public void setClientRegistrationRepository(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }
}