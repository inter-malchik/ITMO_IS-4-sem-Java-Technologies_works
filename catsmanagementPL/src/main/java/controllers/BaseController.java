package controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/authentication")
    public Object authentication(@CurrentSecurityContext(expression = "authentication")
                                 Authentication authentication) {
        return authentication.getDetails();
    }

    @RequestMapping("/")
    public String greetings(@CurrentSecurityContext(expression = "authentication.name")
                            String username) {
        return "Hello, " + username + "!<br>"
                + "...................／＞　 フ.....................<br>" +
                "　　　　　| 　_　 _|<br>" +
                "　 　　　／`ミ _x 彡<br>" +
                "　　 　 /　　　 　 |<br>" +
                "　　　 /　 ヽ　　 ﾉ<br>" +
                "　／￣|　　 |　|　|<br>" +
                "　| (￣ヽ＿_ヽ_)_)<br>" +
                "　＼二つ";
    }

    @PreAuthorize("hasAuthority('ROLE_ONLY_AUTHORIZED')")
    @RequestMapping("/bp-user")
    public String userBasePage() {
        return "Hello, user";
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @RequestMapping("/bp-owner")
    public String ownerBasePage() {
        return "Hello, owner";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/bp-admin")
    public String adminBasePage() {
        return "Hello, admin";
    }
}
