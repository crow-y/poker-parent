package org.tc.moonlighting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Crow on 2018/1/8.
 *
 * @author Crow
 */
@Controller
public class SocketMessageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/lobby")
    public String lobby() {
        return "gameLobby";
    }

}
