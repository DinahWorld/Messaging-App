package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.inbox.folder.Folder;
import io.javabrains.inbox.inbox.folder.FolderRepository;
import io.javabrains.inbox.inbox.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailRepository emailRepository;

    @GetMapping(value = "/email/{id}")
    public String emailView(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {
        //Check if the user  is looged
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folder
        //get the idt
        String userId = principal.getAttribute("login");
        //get the correct folder by user
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (!optionalEmail.isPresent()) {
            return "inbox-page";
        }

        Email email = optionalEmail.get();
        String toIds = String.join(", ",email.getTo());
        model.addAttribute("email", email);
        model.addAttribute("toIds", email);

        return "email-page";

    }

}
