package toyrental.domain;


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
        PaymentService paymentService =  RentalApplication.applicationContext.
        getBean(toyrental.external.PaymentService.class);
        paymentService.pay(payment);
       
        System.out.println("External paymentService call end, rentalId=" + this.rentalId );

    }

    
    

    public static void updateRentalStatus(Accepted accepted){
        try{
            //Rental rental = new Rental();
            /*
            LOGIC GOES HERE
            */
            //repository().save(rental);

            Optional<Rental> optionalRental = repository().findByRentalId(accepted.getRentalId());
            optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
            Rental rental = optionalRental.get();

            rental.setRentalStatus("rent complete");
            repository().save(rental);
            
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /*
    public void setRentalStatus(String str){
        this.rentalStatus = str;
    }
    */
}
