package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethod(key = "sign in privileged")
    public String signInPrivileged(String username, String password) {
        Optional<UserDto> userDto = userService.login(username, password);
        if (userDto.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return "Logged in successfully";
    }

    @ShellMethod(key = "sign out")
    public String signOut() {
        Optional<UserDto> userDto = userService.logout();
        if (userDto.isEmpty()) {
            return "You need to log in first!";
        }
        return userDto.get() + "is logged out.";
    }

    @ShellMethod(key = "describe account")
    public String describeAccount() {
        Optional<UserDto> userDto = userService.describe();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        if (userDto.get().getRole() == User.Role.ADMIN) {
            return "Signed in with privileged account " + userDto.get().getUsername();
        }
        return "Signed in with account " + userDto.get().getUsername();
        //TODO
    }

    @ShellMethod(key = "sign up")
    public String signUp(String username, String password) {
        try {
            userService.registerUser(username, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Signing up failed!";
        }
    }

    @ShellMethod(key = "sign in")
    public String signIn(String username, String password) {
        Optional<UserDto> userDto = userService.login(username, password);
        if (userDto.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return "Logged in successfully!";
    }

}
