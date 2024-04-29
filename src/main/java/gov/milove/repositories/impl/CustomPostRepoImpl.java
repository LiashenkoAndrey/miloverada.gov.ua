package gov.milove.repositories.impl;

import gov.milove.domain.dto.DocumentGroupDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.domain.forum.Post;
import gov.milove.domain.forum.PostLikesInfo;
import gov.milove.domain.forum.PostRecord;
import gov.milove.repositories.forum.CustomPostRepo;
import gov.milove.util.EntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.extern.log4j.Log4j2;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Log4j2
public class CustomPostRepoImpl {

}
