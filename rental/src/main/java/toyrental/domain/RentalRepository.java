package toyrental.domain;

import toyrental.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel="rentals", path="rentals") 
public interface RentalRepository extends PagingAndSortingRepository<Rental, Integer>{

    Optional<Rental> findByRentalId(Integer rentalId); 
}
