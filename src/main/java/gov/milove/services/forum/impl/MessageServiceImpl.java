package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.ForwardMessagesDto;
import gov.milove.domain.dto.forum.MessageDto;
import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.message.ForwardedMessage;
import gov.milove.domain.forum.message.Message;
import gov.milove.repositories.jpa.contact.ForwardedMessageRepository;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.MessageRepo;
import gov.milove.services.forum.MessageImageService;
import gov.milove.services.forum.MessageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static gov.milove.util.Util.decodeUriComponent;
import static java.lang.String.format;


@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepo messageRepo;

    private final ForumUserRepo forumUserRepo;

    private final ChatRepo chatRepo;

    private final MessageImageService imageService;

    private final MessageImageService messageImageService;

    private final SimpMessagingTemplate messagingTemplate;
    private final ForwardedMessageRepository forwardedMessageRepo;


    @PersistenceContext
    private EntityManager em;

    @Override
    public Message saveNewMessage(MessageDto dto) {
        log.info("new message: " + dto);
        Message saved = saveMessage(dto);

        saved.setFileDtoList(dto.getFileDtoList());
        messagingTemplate.convertAndSend("/chat/" + dto.getChatId(), saved);
        JSONObject messageIsSavedPayload = new JSONObject();
        messageIsSavedPayload.put("messageId", saved.getId());

        messagingTemplate.convertAndSend(format("/chat/%s/messageIsSaved?senderId=%s", dto.getChatId(), dto.getSenderId()), messageIsSavedPayload.toJSONString());
        return saved;
    }

    @Override
    public void forwardMessages(ForwardMessagesDto dto) {
        List<Long> messagesIdList = dto.getMessagesIdList();

        for (Long chatId : dto.getToChatsIdList()) {
            // map id list to new Message entity with appropriated chat id and forward message
            List<Message> messages = messagesIdList.stream()
                    .map((messageId) -> {
                        Message message = new Message(chatId, forumUserRepo.getReferenceById(decodeUriComponent(dto.getEncodedSenderId())));
                        log.info("save message");
                        Message saved = messageRepo.save(message);
                        log.info("save forwardedMessage");
                        ForwardedMessage forwardedMessageSaved = forwardedMessageRepo.save( new ForwardedMessage(saved.getId(), messageRepo.getReferenceById(messageId)));

                        saved.setForwardedMessage(forwardedMessageSaved);
                        Message res = messageRepo.save(saved);
                        log.info("save ok {}, {}", res.getId(), res.getForwardedMessage());
                        return res;
//                        message.setForwardedMessage(forwardedMessage);
//                        forwardedMessage.setMessageId(message.getId());
                    })
                    .toList();
            log.info("save messaged for chat {}, list: {}", chatId, messages);
            List<Message> savedMessages = messageRepo.saveAll(messages);
            log.info("messages saved, notify all");
            messagingTemplate.convertAndSend("/chat/" + chatId + "/newForwardedMessagesEvent" , savedMessages);
        }
    }

    @Override
    @Transactional
    public List<Message> getMessages(MessageRequestDto dto) {
        log.info("getMessages: " +dto);

        if (dto.getLastReadMessageId() == null) {
            return loadMessagesByDefault(dto);
        }

        return loadMessagesByLastReadMessage(dto);
    }

    private List<Message> loadMessagesByLastReadMessage(MessageRequestDto dto) {
        log.info("last msg not null: " + dto.getLastReadMessageId());
        Integer pageSize = dto.getPageSize();
        int halfOfPage = pageSize / 2;

        List<Message> messagesBefore = getBefore(dto, halfOfPage);

        int messagesAfterListSize;
        boolean beforeListSizeIsExpectedSize = messagesBefore.size() == halfOfPage;

        if (!beforeListSizeIsExpectedSize) {
            log.info("beforeListSizeIs not expectedSize {} == {}", messagesBefore.size(), halfOfPage);
            messagesAfterListSize = differencePlusSize(messagesBefore.size(), halfOfPage);
        } else {
            log.info("beforeListSizeIsExpectedSize {} == {}", messagesBefore.size(), halfOfPage);
            messagesAfterListSize = halfOfPage;
        }

        List<Message> messagesAfter = getAfter(dto, messagesAfterListSize);
        boolean afterListIsExpectedSize = messagesAfter.size() == halfOfPage;

        if (!afterListIsExpectedSize && beforeListSizeIsExpectedSize) {
            log.info("!afterListIsExpectedSize && beforeListSizeIsExpectedSize  messagesAfter:{}, messagesBefore:{}", messagesAfter.size(), messagesBefore.size());
            int newMessagesBeforeListSize = differencePlusSize(messagesAfter.size(), halfOfPage);
            List<Message> newMessagesBefore = getBefore(dto, newMessagesBeforeListSize);
            log.info("newMessagesBeforeListSize = {}", newMessagesBefore.size());
            return Stream.concat(newMessagesBefore.stream(), messagesAfter.stream()).toList();
        }

        return Stream.concat(messagesBefore.stream(), messagesAfter.stream()).toList();
    }

    private List<Message> loadMessagesByDefault(MessageRequestDto dto) {
        log.info("last msg is null, load default page");
        Pageable pageReq = PageRequest.of(
                dto.getPageIndex(),
                dto.getPageSize(),
                Sort.by("createdOn")
                        .descending()
        );

        List<Message> messages = messageRepo.findAllByChatId(dto.getChatId(), pageReq);
        Collections.reverse(messages);
        return messages;
    }
    private int differencePlusSize(int n1, int size) {
        return (size - n1) + size;
    }

    private List<Message> getBefore(MessageRequestDto dto, int listSize) {
        List<Message> messages = em.createQuery("from Message m where m.id < :id and m.chatId = :chatId order by m.createdOn desc", Message.class)
                .setParameter("chatId", dto.getChatId())
                .setParameter("id", dto.getLastReadMessageId())
                .setMaxResults(listSize)
                .getResultList();

        Collections.reverse(messages);
        return messages;
    }

    private List<Message> getAfter(MessageRequestDto dto, int listSize) {
        return em.createQuery("from Message m where m.id >= :id and m.chatId = :chatId order by m.createdOn", Message.class)
                .setParameter("chatId", dto.getChatId())
                .setParameter("id", dto.getLastReadMessageId())
                .setMaxResults(listSize)
                .getResultList();
    }


    @Override
    @Transactional
    public Long deleteById(Long id) {
        Message message = messageRepo.findById(id).orElseThrow(EntityNotFoundException::new);

        messageImageService.deleteImagesIfNotUsedMoreThenOneTime(message.getImagesList());
        chatRepo.deleteFromLastReadMessagesTable(id);
        messageRepo.deleteById(id);
        return id;
    }



    @Override
    public Message saveMessage(MessageDto dto) {
        Message message = MessageDto.toEntity(dto);
        message.setSender(forumUserRepo.getReferenceById(dto.getSenderId()));
        message.setChatId(dto.getChatId());

        if (dto.getReplyToMessageId() != null) {
            Message message1 = messageRepo.getReferenceById(dto.getReplyToMessageId());
            log.info("is reply! {}, reply message={}", dto.getReplyToMessageId(), message1);

            message.setRepliedMessage(message1);
        }

        if (!dto.getImagesDtoList().isEmpty()) {
            message.setImagesList(imageService.saveImages(dto.getImagesDtoList()));
        }
        Message saved = messageRepo.save(message);
        Message fetched = messageRepo.findById(saved.getId()).orElseThrow(EntityNotFoundException::new);
        log.info("fetched = {}", fetched);
        return fetched;
    }
}
