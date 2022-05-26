package toyrental.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="store", url="${api.path.store}")
public interface StoreService {
    @RequestMapping(method= RequestMethod.GET, path="/stores/{toyId}")
    public Store getStore(@PathVariable("toyId") Integer toyId);

}

