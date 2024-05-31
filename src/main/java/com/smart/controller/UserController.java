package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.models.ContactDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println("User " + user);
        model.addAttribute("user", user);
    }

    //dashboard home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title","Dashboard");
        return "normal/user_dashboard";
    }


    //open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model, HttpSession session){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());

        if ( Objects.nonNull(session.getAttribute("message")) && StringUtils.hasText(session.getAttribute("message").toString())) {
            session.removeAttribute("message");
        }

        return "normal/add_contact_form";
    }

    // processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute ContactDTO contact, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session){
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            Contact contactEntity = new Contact();

            //processing and uploading file...
            if(file.isEmpty()){
                //if file empty then try our message
                System.out.println("file is empty");
            } else {
                //save the file to a folder and update the name to contact
                contactEntity.setProfileImage(file.getOriginalFilename());

                contactEntity.setName(contact.getName());
                contactEntity.setSecondName(contact.getSecondName());
                contactEntity.setEmail(contact.getEmail());
                contactEntity.setPhone(contact.getPhone());
                contactEntity.setWork(contact.getWork());
                contactEntity.setDescription(contact.getDescription().trim());

                File saveFile = new File("static/img");

                Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
            }

            user.getContacts().add(contactEntity);
//            contactEntity.setUser(user);

            this.userRepository.save(user);

            System.out.println("Data " + contact);
            System.out.println("added to database");
            // success msg
            session.setAttribute("message", new Message("Your contact is added,you can add more..","success"));

        } catch(Exception e){
            System.out.println("ERROR "+ e.getMessage());
            e.printStackTrace();
            // error msg
            session.setAttribute("message", new Message("Something went wrong, try again","danger"));
        }
        return "normal/add_contact_form";
    }

}
