package com.work.plat.controller.im;

import com.work.plat.socket.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/chat")
public class ChatController {


    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseBody
    public String send(String message) {
        try {
            WebSocketServer.sendInfo("公告:" + message);
        } catch (IOException e) {

        }
        return "success!";
    }
}
