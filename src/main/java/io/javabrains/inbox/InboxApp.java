package io.javabrains.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.inbox.folder.Folder;
import io.javabrains.inbox.inbox.folder.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
@RestController
public class InboxApp {

    @Autowired
    FolderRepository folderRepository;
    @Autowired
    EmailListItemRepository emailListItemRepository;
    @Autowired
    EmailRepository emailRepository;

    public static void main(String[] args) {
        SpringApplication.run(InboxApp.class, args);
    }


    // Connect to Astra with creating DataStaxAstraProperties
    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("DinahWorld", "Inbox", "blue"));
        folderRepository.save(new Folder("DinahWorld", "Sent", "green"));
        folderRepository.save(new Folder("DinahWorld", "Important", "yellow"));

        for (int i = 0; i < 10; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId("DinahWorld");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem item = new EmailListItem();
            item.setKey(key);
            item.setTo(List.of("DinahWorld","Me","Someone"));
            item.setSubject("Subject  " + i);
            item.setUnread(true);

            emailListItemRepository.save(item);

            Email email = new Email();
            email.setId(key.getTimeUUID());
            email.setFrom("DinahWorld");
            email.setSubject(item.getSubject());
            email.setBody("Body " + i);
            email.setTo(item.getTo());

            emailRepository.save(email);
        }
    }

}
