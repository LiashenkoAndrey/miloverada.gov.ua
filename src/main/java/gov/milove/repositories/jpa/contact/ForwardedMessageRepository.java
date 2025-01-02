package gov.milove.repositories.jpa.contact;

import gov.milove.domain.forum.message.ForwardedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForwardedMessageRepository extends JpaRepository<ForwardedMessage, Long> {
}
