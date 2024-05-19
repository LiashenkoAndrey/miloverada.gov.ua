package gov.milove.repositories.jpa;

import gov.milove.domain.LinkBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkBannerRepository extends JpaRepository<LinkBanner, Long> {
}
