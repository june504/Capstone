package toyrental.domain;

import toyrental.RentalApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Rental_table")
@Data

public class Rental  {

    
    
    private Integer rentalId;
    
    
    private Integer customerId;
    
    
    private Integer toyId;
    
    
    private String rentalStatus;

    @PostPersist
    public void onPostPersist(){
    }


    public static RentalRepository repository(){
        RentalRepository rentalRepository = RentalApplication.applicationContext.getBean(RentalRepository.class);
        return rentalRepository;
    }

    public static void return(){
        ToyReturned toyReturned = new ToyReturned();
        /*
        Input Event Content
        */
        toyReturned.publishAfterCommit();

    }

    public static void updateRentalStatus(Accepted accepted){

        Rental rental = new Rental();
        /*
        LOGIC GOES HERE
        */
        // repository().save(rental);


    }


}
