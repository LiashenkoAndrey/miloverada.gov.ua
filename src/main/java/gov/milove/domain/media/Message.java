package gov.milove.domain.media;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {


    private String senderName;
    private String message;
    private String targetUserName;
}

