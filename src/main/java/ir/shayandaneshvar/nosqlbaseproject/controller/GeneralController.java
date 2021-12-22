package ir.shayandaneshvar.nosqlbaseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneralController {
    @RequestMapping("/game")
    public String game() {
        return "redirect:/game.html";
    }

    @RequestMapping({"/", "/index", "index.do", "index.html"})
    public String index() {
        return "index";
    }
}
