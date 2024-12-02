package gov.milove.services.forum;

import gov.milove.domain.dto.forum.NewTopicChatDto;
import gov.milove.domain.dto.forum.PrivateChatDto;
import gov.milove.domain.dto.forum.TopicChatDto;

public interface ChatService {

    /**
     * Adds new chat to visited users chats list
     * or updates last visited time of existing chat by user
     *
     * @param forumUserId id of forum user
     * @param chatId chat id
     * @since 2.0
     */
    void addChatToVisitedUsersChatsOrUpdateIfExists(String forumUserId, Long chatId);


    /**
     * Calls when user want open or start a private chat with other user
     * @param receiverId user which started chat
     * @param senderId user that wants start chat
     * @return {@link PrivateChatDto}
     * @since 2.0
     */
    PrivateChatDto findPrivateChatBetweenToUsers(String receiverId, String senderId);

    TopicChatDto newTopicChat(NewTopicChatDto dto);
}
