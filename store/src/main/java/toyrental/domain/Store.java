package toyrental.domain;

import toyrental.domain.Registered;
import toyrental.StoreApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Store_table")
@Data

public class Store  {

    
    
    private Integer toyId;
    
    
    private String rentalId;
    
    
    private String name;
    
    
    private Integer toyRentalPrice;
    
    
    private String toyStatus;

    @PostPersist
    public void onPostPersist(){
        Registered registered = new Registered();
        BeanUtils.copyProperties(this, registered);
        registered.publishAfterCommit();

    }


    public static StoreRepository repository(){
        StoreRepository storeRepository = StoreApplication.applicationContext.getBean(StoreRepository.class);
        return storeRepository;
    }

    public static void repairRequest(){
        RepairRequested repairRequested = new RepairRequested();
        /*
        Input Event Content
        */
        repairRequested.publishAfterCommit();

    }
    public static void accept(){
        Accepted accepted = new Accepted();
        /*
        Input Event Content
        */
        accepted.publishAfterCommit();

    }
    public static void returnConfirm(){
        ReturnConfirmed returnConfirmed = new ReturnConfirmed();
        /*
        Input Event Content
        */
        returnConfirmed.publishAfterCommit();

    }

    public static void rentalConfirm(Paid paid){

        Store store = new Store();
        /*
        LOGIC GOES HERE
        */
        // repository().save(store);


    }
    public static void rentalCancel(PayCancelled payCancelled){

        Store store = new Store();
        /*
        LOGIC GOES HERE
        */
        // repository().save(store);


    }
    public static void toyReturn(ToyReturned toyReturned){

        Store store = new Store();
        /*
        LOGIC GOES HERE
        */
        // repository().save(store);


    }
    public static void updateToyStatus(ToyRepaired toyRepaired){

        Store store = new Store();
        /*
        LOGIC GOES HERE
        */
        // repository().save(store);


    }
    public static void updateToyStatus(Discarded discarded){

        Store store = new Store();
        /*
        LOGIC GOES HERE
        */
        // repository().save(store);


    }


}
