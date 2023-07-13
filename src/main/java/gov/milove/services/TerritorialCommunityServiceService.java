package gov.milove.services;

import gov.milove.domain.digitalQueue.Service;

import java.util.List;

public interface TerritorialCommunityServiceService {

   void save(Service service);

   List<Service> findAll();

}
