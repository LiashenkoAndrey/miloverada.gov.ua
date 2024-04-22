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
public class CustomPostRepoImpl implements CustomPostRepo {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public Object findPostWithLikesInfoById(Long id) {
        log.info("findPostWithLikesInfoById");
        List<PostRecord> tuples = em.createQuery("""
    select p.text as text, p.imageId as imageId, p.createdOn as createdOn, p.author as author, (select count(*) from PostLike pl where pl.post.id = p.id) as likesAmount from Post p where p.id = :id
""").unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuple, aliases) -> {
                    int i =0;
                    return new PostRecord(
                            new Post(
                                    (Long) tuple[i++],
                                    (ForumUser) tuple[i++],
                                    (String) tuple[i++],
                                    (String) tuple[i++],
                                    (Date) tuple[i++]
                            ),

                            new PostLikesInfo(
                                    true, 4L
                            )
                    );
                }).getResultList();
        log.info("AFTER");
        log.info(tuples);
        return List.of();
    }
}
