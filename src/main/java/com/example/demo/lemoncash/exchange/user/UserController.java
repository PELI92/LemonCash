package com.example.demo.lemoncash.exchange.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getById(Long.valueOf(id));
    }

    @GetMapping("/alias/{alias}")
    public User getUserByAlias(@PathVariable String alias) {
        return userService.getByAlias(alias);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) throws Exception {
        userService.create(userRequest);
        return ResponseEntity.ok("Successfully created user");
    }

}
