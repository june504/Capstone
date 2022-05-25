package toyrental.infra;
import toyrental.domain.*;
import toyrental.external.*;

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
        public Rental returnToy(@PathVariable(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
                    System.out.println("##### /rental/return  called #####");
                    Optional<Rental> optionalRental = rentalRepository.findById(id);
                    
                    optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
                    Rental rental = optionalRental.get();
                    rental.returnToy();
                    
                    rentalRepository.save(rental);
                    return rental;
                
        }


        @RequestMapping(value = "/{id}/rentalcancel",
                method = RequestMethod.PUT,
                produces = "application/json;charset=UTF-8")
        public Rental rentalCancel(@PathVariable(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /rental/rentalcancel  called #####");
                        Optional<Rental> optionalRental = rentalRepository.findById(id);
                        
                        optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
                        Rental rental = optionalRental.get();
                        rental.rentalCancel();
                        
                        rentalRepository.save(rental);
                        return rental;
                        
                }

        @RequestMapping(value = "/rent",
                method = RequestMethod.POST,
                produces = "application/json;charset=UTF-8")
        public Rental rentToy(HttpServletRequest request, HttpServletResponse response)
                throws Exception {
                        System.out.println("##### /rental/rent  called #####"+request.getParameter("customerId"));
                        
                        Rental rental = new Rental();
                        rental.setCustomerId(new Integer(request.getParameter("customerId")));
                        rental.setToyId(new Integer(request.getParameter("toyId")));
                        rental.setRentalStatus(request.getParameter("rentalStatus"));
                                               
                        rentalRepository.save(rental);
                        return rental;
                }
        
 }
