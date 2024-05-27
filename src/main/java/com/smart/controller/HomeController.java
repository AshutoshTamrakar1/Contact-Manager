package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home- Smart contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About- Smart contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Signup- Smart contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    //handler for registering user

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session, RedirectAttributes redirectAttributes){

        try{
            if (!agreement){
                System.out.println("you have not agreed to terms");
                throw new Exception("you have not agreed to terms");
            }

            if(result1.hasErrors()){
                System.out.println("ERROR "+result1.toString());
                model.addAttribute("user",user);
                return "redirect:signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println("agreement " +agreement);
            System.out.println("user " +user);

            User result = this.userRepository.save(user);

            // Use Flash Attribute instead of Session Attribute
            redirectAttributes.addFlashAttribute("message", new Message("successfully registered...","alert-success"));
            return "redirect:signup";
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            // Use Flash Attribute instead of Session Attribute
            redirectAttributes.addFlashAttribute("message", new Message("Something went wrong.." +e.getMessage(),"alert-danger"));
            return "redirect:signup";
        }
    }

    //handler for custom login
    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title", "Signin- Smart contact Manager");
        return "login";
    }



}
