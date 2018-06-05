package nl.yschuurmans.MessageServer.repositories;

import nl.yschuurmans.MessageServer.domain.ConfirmMessage;
import nl.yschuurmans.MessageServer.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
