package gov.milove.domain.dto.document.media;

import gov.milove.domain.media.LinkBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkBannerRepository extends JpaRepository<LinkBanner, Long> {
}
