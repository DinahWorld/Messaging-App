package io.javabrains.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
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
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@RestController
public class InboxApp {

	@Autowired FolderRepository folderRepository;
	@Autowired
	EmailListItemRepository emailListItemRepository;
	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}


	// Connect to Astra with creating DataStaxAstraProperties
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties){
			Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	@PostConstruct
	public void init(){
		folderRepository.save(new Folder("DinahWorld", "Inbox" , "blue"));
		folderRepository.save(new Folder("DinahWorld", "Sent" , "green"));
		folderRepository.save(new Folder("DinahWorld", "Important" , "yellow"));

		for(int i = 0;i < 10; i++){
			EmailListItemKey key = new EmailListItemKey();
			key.setId("DinahWorld");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());

			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setTo(Arrays.asList("DinahWorld"));
			item.setSubject("Subject  " + i);
			item.setUnread(true);

			emailListItemRepository.save(item);
		}
	}

}
