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
 @RequestMapping(value="/rentals")
 @Transactional
 public class RentalController {
        @Autowired
        RentalRepository rentalRepository;




        @RequestMapping(value = "/{id}/return",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Rental return(@PathVariable(value = "id") Long id, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /rental/return  called #####");
                        Optional<Rental> optionalRental = rentalRepository.findById(id);
                        
                        optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
                        Rental rental = optionalRental.get();
                        rental.return();
                        
                        rentalRepository.save(rental);
                        return rental;
                        
                }

        
 }
