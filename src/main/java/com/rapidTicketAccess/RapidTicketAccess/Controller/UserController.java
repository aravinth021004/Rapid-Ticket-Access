package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Model.Users;
import com.rapidTicketAccess.RapidTicketAccess.Service.UsersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UsersService usersService;

    UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public String register(@RequestBody Users user) throws Exception {
        usersService.register(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) throws Exception {
        return usersService.verify(user);
    }
}
