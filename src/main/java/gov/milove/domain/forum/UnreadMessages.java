package gov.milove.domain.forum;

import gov.milove.domain.AppUser;
import gov.milove.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "forum")
public class UnreadMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private String userId;
    private Long messageId;
}
