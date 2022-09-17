package io.javabrains.inbox.inbox.folder;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//CassandraRepository is a generic type
//send two type of arguments
// first type = entity itself = Folder
// second type of the entity or primary key = userId = String

// We have now both the entity (Folder) and the repository to do the work
// of fetching data
@Repository
public interface FolderRepository extends CassandraRepository<Folder,String> {
    List<Folder> findAllById(String id);
}
