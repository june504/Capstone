package toyrental.domain;

import toyrental.domain.Paid;
import toyrental.domain.PayCancelled;
import toyrental.PaymentApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Payment_table")
@Data

public class Payment  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    private Integer payId;
    
    
    private Integer rentalId;
    
    
    private String payStatus;
    
    
    private Integer toyRentalPrice;

    @PostPersist
    public void onPostPersist(){
        Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();

    }
    @PostUpdate
    public void onPostUpdate(){
        PayCancelled payCancelled = new PayCancelled();
        BeanUtils.copyProperties(this, payCancelled);
        payCancelled.publishAfterCommit();

    }


    public static PaymentRepository repository(){
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }


    public static void payCancel(RentalCancelled rentalCancelled){

        Payment payment = new Payment();
        /*
        LOGIC GOES HERE
        */
        // repository().save(payment);

        // PayCancelled payCancelled = new PayCancelled();
        /*
        Input Event Content
        */
        // payCancelled.publishAfterCommit();


    }


}
