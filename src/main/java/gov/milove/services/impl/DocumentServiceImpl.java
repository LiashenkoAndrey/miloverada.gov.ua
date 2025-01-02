package gov.milove.services.impl;

import gov.milove.domain.document.Document;
import gov.milove.domain.document.DocumentGroup;
import gov.milove.domain.mongo.MongoDocument;
import gov.milove.exceptions.ServiceException;
import gov.milove.repositories.jpa.document.DocumentGroupRepo;
import gov.milove.repositories.jpa.document.DocumentRepo;
import gov.milove.repositories.mongo.MongoDocumentRepo;
import gov.milove.services.document.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentServiceImpl implements DocumentService {


    private final DocumentRepo documentRepo;
    private final MongoDocumentRepo mongoDocumentRepo;
    private final DocumentGroupRepo groupRepository;

    @Override
    public Document saveDocument(Long groupId, MultipartFile file, String title) {
        Optional<Document> documentOpt = documentRepo.findByHashCode(file.hashCode());
        log.info("save or get document with filename - {}", file.getOriginalFilename());
        if (documentOpt.isPresent()) {
            log.info("document already exists");
            Document document = documentOpt.get();
            document.setTitle(title);
            document.setName(file.getOriginalFilename());
            addToGroupAndReturn(documentOpt.get(), groupId);
            return document;
        }

        return save(groupId, file, title);
    }

    private void addToGroupAndReturn(Document document, Long groupId) {
        DocumentGroup group = groupRepository.findById(groupId).orElseThrow(EntityNotFoundException::new);
        group.getDocuments().add(document);
        groupRepository.save(group);
    }

    @Override
    public void delete(Document document) {
        if (!documentRepo.documentUsedMoreThenOneTime(document.getName())) {
            log.info("delete document = {}", document);
            if (document.getMongoId() != null) {
                log.info("mongo id not null - {}", document.getMongoId());
                documentRepo.delete(document);
                mongoDocumentRepo.deleteById(document.getMongoId());
                return;
            }
            log.info("mongo is null, delete by filename - {}", document.getName());
            mongoDocumentRepo.deleteByFilename(document.getName());

        } else log.info("document used more than one time = {}", document);
    }

    @Override
    public void deleteAll(List<Document> documents) {
        for (Document document : documents) {
            delete(document);
        }
    }


    private Document save(Long groupId, MultipartFile file, String title) {
        try {
            log.info("a document doesn't exist");
            byte[] bytes = file.getBytes();

            MongoDocument mongoDocument = new MongoDocument(file.getOriginalFilename(), new Binary(bytes), file.getContentType());
            MongoDocument savedMongo = mongoDocumentRepo.save(mongoDocument);
            log.info("document saved to mongo = {}", savedMongo);

            Document document = Document.builder()
                    .mongoId(savedMongo.getId())
                    .documentGroup(groupRepository.getReferenceById(groupId))
                    .name(file.getOriginalFilename())
                    .title(title)
                    .hashCode(Arrays.hashCode(bytes))
                    .build();
            Document savedDoc = documentRepo.save(document);
            log.info("a document is saved - {}", document);
            return savedDoc;
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
