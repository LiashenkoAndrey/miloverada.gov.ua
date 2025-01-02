package gov.milove.repositories.jpa;

import gov.milove.domain.forum.ForwardedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForwardedMessageRepo extends JpaRepository<ForwardedMessage, Long> {
}
