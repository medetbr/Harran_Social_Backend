package com.edu.harran.social.websocket.repository;

import com.edu.harran.social.websocket.entity.Document;
import com.edu.harran.social.websocket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    Document findByMessage(Message msg);

    Document findByFileName(String fileName);
}
