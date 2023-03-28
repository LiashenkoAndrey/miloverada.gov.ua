package gov.milove.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for administrator
 */
@Controller
@RequestMapping("/admin")
public class Admin {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", defaultValue = "false") boolean isError,
                        HttpServletRequest request,
                        Model model) {
        String errorMessage = null;
        HttpSession httpSession = request.getSession(false);
        if (isError) {
            var exception = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) errorMessage = exception.getMessage();
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
}
