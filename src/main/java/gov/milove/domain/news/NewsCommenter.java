package gov.milove.domain.news;


import gov.milove.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "news_commenter")
public class NewsCommenter extends User {


    @Builder
    public NewsCommenter(String id, @NotBlank String firstName, @Email String email) {
        super(id, firstName, email);
    }
}
