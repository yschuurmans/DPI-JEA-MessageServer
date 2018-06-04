package nl.yschuurmans.MessageServer.repositories;

import nl.yschuurmans.MessageServer.domain.ConfirmMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmMessageRepo extends JpaRepository<ConfirmMessage, Long> {
}
