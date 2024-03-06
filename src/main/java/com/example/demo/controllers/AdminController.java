package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String showAllUser(Model model, Principal principal) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("user", userService.findUserByUsername(principal.getName()));
        model.addAttribute("roles", roleService.getAllRole());
        return "admin";
    }

    @PostMapping("/add")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("roles", roleService.getAllRole());
        if (bindingResult.hasErrors()){
            return "redirect:/admin";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute(value = "user") @Valid User user, BindingResult result) {
        if (result.hasErrors())
            return "redirect:/admin";



        userService.updateUser(user, user.getPassword());
        return "redirect:/admin";
    }

    @PostMapping("/users")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

}