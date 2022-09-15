package io.javabrains.inbox.controllers;

import io.javabrains.inbox.inbox.folder.Folder;
import io.javabrains.inbox.inbox.folder.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// Will detect if the user is logged or not
@Controller
public class InboxController {

    @Autowired private FolderRepository folderRepository;
    @GetMapping(value =  "/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        //Check if the user  is looged
        if(principal ==  null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        //get the id
        String userId = principal.getAttribute("login");
        //get the correct folder by user
        List<Folder> userFolders = folderRepository.findAllById(userId);


        model.addAttribute("userFolders",userFolders);
        System.out.println(userId);
        return "inbox-page";
    }
}
