package toyrental.domain;

import toyrental.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="repairs", path="repairs")
public interface RepairRepository extends PagingAndSortingRepository<Repair, Integer>{


}
