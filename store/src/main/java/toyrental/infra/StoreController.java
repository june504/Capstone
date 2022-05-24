package toyrental.infra;
import toyrental.domain.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

 @RestController
 @RequestMapping(value="/stores")
 @Transactional
 public class StoreController {
        @Autowired
        StoreRepository storeRepository;



        @RequestMapping(value = "/{id}/repairrequest",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Store repairRequest(@PathVariable(value = "id") Long id, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /store/repairRequest  called #####");
                        Optional<Store> optionalStore = storeRepository.findById(id);
                        
                        optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
                        Store store = optionalStore.get();
                        store.repairRequest();
                        
                        storeRepository.save(store);
                        return store;
                        
                }

        


        @RequestMapping(value = "/{id}/accept",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Store accept(@PathVariable(value = "id") Long id, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /store/accept  called #####");
                        Optional<Store> optionalStore = storeRepository.findById(id);
                        
                        optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
                        Store store = optionalStore.get();
                        store.accept();
                        
                        storeRepository.save(store);
                        return store;
                        
                }

        


        @RequestMapping(value = "/{id}/returnconfirm",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Store returnConfirm(@PathVariable(value = "id") Long id, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /store/returnConfirm  called #####");
                        Optional<Store> optionalStore = storeRepository.findById(id);
                        
                        optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
                        Store store = optionalStore.get();
                        store.returnConfirm();
                        
                        storeRepository.save(store);
                        return store;
                        
                }

        
 }
