package toyrental.infra;

import toyrental.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToyListRepository extends CrudRepository<ToyList, Integer> {


}