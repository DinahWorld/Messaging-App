package io.javabrains.inbox;

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

@SpringBootApplication
@RestController
public class InboxApp {

	@Autowired FolderRepository folderRepository;
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
	}

}
