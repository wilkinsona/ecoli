package ecoli.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Jonatan Ivanov
 */
@Controller
public class EcoliController {
    @RequestMapping(path = "/echo", method = GET)
    public String echo(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "echo";
    }
}
