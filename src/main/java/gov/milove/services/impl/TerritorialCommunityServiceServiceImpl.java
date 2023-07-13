package gov.milove.services.impl;

import gov.milove.domain.digitalQueue.Service;
import gov.milove.repositories.ServiceRepository;
import gov.milove.services.TerritorialCommunityServiceService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class TerritorialCommunityServiceServiceImpl implements TerritorialCommunityServiceService {

    private final ServiceRepository repository;

    @Override
    public void save(Service service) {
        repository.save(service);
    }

    @Override
    public List<Service> findAll() {
        return repository.findAll();
    }

}
