package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService,RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users",userService.findAllUsers());
        return "/admin/allUsers";
    }

    @GetMapping("/new")
    public String getNewUserForm(Model model) {
        model.addAttribute("user",new User());
        model.addAttribute("listRoles",roleService.getListRoles());
        return "/admin/new";
    }

    @PostMapping("/new")
    public String addNewUser(@ModelAttribute("user") User user,@RequestParam(value = "role") String role) {
        user.setRoles(roleService.findRoleByName(role));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") int id,Model model) {
        model.addAttribute("user",userService.findOneUserById(id));
        return "/admin/show";
    }

    @GetMapping("/edit/{id}")
    private String getUpdatedUserData(@PathVariable("id") int id,Model model) {
        model.addAttribute("user",userService.findOneUserById(id));
        model.addAttribute("listRoles",roleService.getListRoles());
        return "admin/edit";
    }

    @PutMapping("/edit")
    public String setUpdatedUserData(@ModelAttribute("user") User user,@RequestParam(value = "role") String role) {
        user.setRoles(roleService.findRoleByName(role));
        userService.updateUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}