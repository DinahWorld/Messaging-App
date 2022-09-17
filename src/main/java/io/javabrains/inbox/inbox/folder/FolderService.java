package io.javabrains.inbox.inbox.folder;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {
    // Default folder
    public List<Folder> fetchDefaultFolders(String userId) {
        return Arrays.asList(
                new Folder(userId,"Inbox","white"),
                new Folder(userId,"Sent Items","green"),
                new Folder(userId,"Important","red")
        );
    }
}
