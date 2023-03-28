package gov.milove.domain;



import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@NoArgsConstructor
@Embeddable
public class Role implements GrantedAuthority {

    public Role(String authority) {
        this.authority = authority;
    }



    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
