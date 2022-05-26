package toyrental.domain;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import toyrental.external.*;
import toyrental.RentalApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name="Rental_table")
@Data
public class Rental  {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer rentalId;       
    private Integer customerId;    
    private Integer toyId;    
    private String rentalStatus;
    private Integer toyRentalPrice; 

    @PrePersist
    public void onPrePersist(){
        try{
            Store store = getToyInfo(this.toyId);
            if(!"AVAILABLE".equals(store.getToyStatus())){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Toy AVAILABLE"); 
            }
            this.toyRentalPrice = store.getToyRentalPrice();
        }catch(FeignException e) {
            if(e.status() == 404){
                //throw new RuntimeException("No Toy Found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Toy Found", e);
            }else{
                throw new RuntimeException(e);
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
    }

    @PostPersist
    public void onPostPersist(){
        ToyRented registered = new ToyRented();
        BeanUtils.copyProperties(this, registered);
        registered.publishAfterCommit();

        System.out.println("ToyRented Event publish");

        toyRental();

    }


    public static RentalRepository repository(){
        RentalRepository rentalRepository = RentalApplication.applicationContext.getBean(RentalRepository.class);
        return rentalRepository;
    }

    public void returnToy(){
        setRentalStatus("return");

        ToyReturned toyReturned = new ToyReturned();
        BeanUtils.copyProperties(this, toyReturned);
        /*
        Input Event Content
        */
        toyReturned.publishAfterCommit();

    }

    public void rentalCancel(){
        setRentalStatus("cancel");
        
        RentalCancelled rentalCancelled = new RentalCancelled();
        BeanUtils.copyProperties(this, rentalCancelled);
        /*
        Input Event Content
        */
        rentalCancelled.publishAfterCommit();

    }

    public void toyRental(){
         
        Payment payment = new Payment();
        payment.setRentalId(this.rentalId);
        payment.setToyId(this.toyId);
        payment.setToyRentalPrice(this.toyRentalPrice);
        System.out.println("### toy Rental Price!! " + this.toyRentalPrice);
        PaymentService paymentService =  RentalApplication.applicationContext.
        getBean(toyrental.external.PaymentService.class);
        paymentService.pay(payment);
       
        System.out.println("External paymentService call end, rentalId=" + this.rentalId );

    }

    public Store getToyInfo(Integer toyId){
       
        Store store = RentalApplication.applicationContext.getBean(toyrental.external.StoreService.class).getStore(toyId);
        return store;

    }
    

    public static void updateRentalStatus1(Accepted accepted){
      
        //Rental rental = new Rental();
        /*
        LOGIC GOES HERE
        */
        //repository().save(rental);

        repository().findByRentalId(accepted.getRentalId())
        .ifPresent(rental-> {
            rental.setRentalStatus("rent complete");
            repository().save(rental);

        });
        //optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
        //Rental rental = optionalRental.get();

        //rental.setRentalStatus("rent complete");
        //repository().save(rental);
    }


    public static void updateRentalStatus2(ReturnConfirmed returnConfirmed){
  
        //Rental rental = new Rental();
        /*
        LOGIC GOES HERE
        */
        //repository().save(rental);

        repository().findByRentalId(returnConfirmed.getRentalId())
        .ifPresent(rental -> {
            rental.setRentalStatus("return complete");
            repository().save(rental);
        });
        //optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
        //Rental rental = optionalRental.get();

        //rental.setRentalStatus("return complete");
        //repository().save(rental);
            
       
    }

    /*
    public void setRentalStatus(String str){
        this.rentalStatus = str;
    }
    */
}
