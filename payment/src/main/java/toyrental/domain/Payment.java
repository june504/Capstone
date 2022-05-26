package toyrental.domain;

import toyrental.domain.Paid;
import toyrental.domain.PayCancelled;
import toyrental.PaymentApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.util.Optional;


@Entity
@Table(name="Payment_table")
@Data

public class Payment  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    

    private Integer payId;
    
    
    private Integer rentalId;
    
    
    private String payStatus;

    
    private Integer toyId;
    
    
    private Integer toyRentalPrice;

    @PrePersist
    public void onPrePersist(){
        this.payStatus = "paid";
    }    

    @PostPersist
    public void onPostPersist(){
        Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();

    }
    
    @PostUpdate
    public void onPostUpdate(){
        

    }


    public static PaymentRepository repository(){
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }

    public void payCancel(){
        setPayStatus("cencel");
    }

    public static void payCancel(RentalCancelled rentalCancelled){
        try{
            Integer rentalId = rentalCancelled.getRentalId();
            Optional<Payment> optionalRental = repository().findByRentalId(rentalId);
            optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
            Payment payment = optionalRental.get();

            payment.setPayStatus("cencel");
            repository().save(payment);
        
            PayCancelled payCancelled = new PayCancelled();
            //BeanUtils.copyProperties(this, payCancelled);
            payCancelled.setPayId(payment.getPayId());
            payCancelled.setToyRentalPrice(payment.getToyRentalPrice());
            payCancelled.setRentalId(rentalCancelled.getRentalId());
            payCancelled.setPayStatus("cencel");

            payCancelled.publish();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
        /*
        LOGIC GOES HERE
        */

        // PayCancelled payCancelled = new PayCancelled();
        /*
        Input Event Content
        */
        // payCancelled.publishAfterCommit();


    }


}
