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
 @RequestMapping(value="/repairs")
 @Transactional
 public class RepairController {
        @Autowired
        RepairRepository repairRepository;


        @RequestMapping(value = "/{repairId}/discard",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Repair discard(@PathVariable(value = "repairId") Integer repairId, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /repair/discard  called #####");
                        Optional<Repair> optionalRepair = repairRepository.findById(repairId);
                        
                        optionalRepair.orElseThrow(()-> new Exception("No Entity Found"));
                        Repair repair = optionalRepair.get();
                        repair.discard();
                        
                        repairRepository.save(repair);
                        return repair;
                        
                }

        


        @RequestMapping(value = "/{repairId}/repair",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Repair repair(@PathVariable(value = "repairId") Integer repairId, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /repair/repair  called #####");
                        Optional<Repair> optionalRepair = repairRepository.findById(repairId);
                        
                        optionalRepair.orElseThrow(()-> new Exception("No Entity Found"));
                        Repair repair = optionalRepair.get();
                        repair.repair();
                        
                        repairRepository.save(repair);
                        return repair;
                        
                }

        
 }
