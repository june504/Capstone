package toyrental.domain;

import toyrental.domain.Registered;
import toyrental.StoreApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Entity
@Table(name="Store_table")
@Data
public class Store  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Integer toyId;  
    
    
    private Integer rentalId;
    
    
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

    public void repairRequest(){
        setToyStatus("UNDER_REPAIR");

        RepairRequested repairRequested = new RepairRequested();
        /*
        Input Event Content
        */
        BeanUtils.copyProperties(this, repairRequested);
        repairRequested.publishAfterCommit();

    }
    /*
    public void accept(){
        setToyStatus("ACCEPTED");
        Accepted accepted = new Accepted();
        BeanUtils.copyProperties(this, accepted);
        accepted.publishAfterCommit();

    }
    */
    public void returnConfirm(){
        setToyStatus("RETURNED");
        ReturnConfirmed returnConfirmed = new ReturnConfirmed();
        BeanUtils.copyProperties(this, returnConfirmed);

        /*
        Input Event Content
        */
        returnConfirmed.publishAfterCommit();

        


    }

    public static void rentalConfirm(Paid paid){
        try{
            Integer toyId = paid.getToyId();
            Optional<Store> optionalStore = repository().findByToyId(toyId);
            optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
            Store store = optionalStore.get();
       
            Accepted accepted = new Accepted();
            //BeanUtils.copyProperties(this, payCancelled);
            accepted.setRentalId(paid.getRentalId());
            accepted.setToyRentalPrice(paid.getToyRentalPrice());
            accepted.setToyId(paid.getToyId());
            accepted.setToyStatus("ACCEPTED");
            
            accepted.publish();

            store.setRentalId(paid.getRentalId());
            store.setToyStatus("ACCEPTED");
            repository().save(store);

        }catch(Exception e){
            throw new RuntimeException(e);
        }
        /*
        repository().findById(paid.getToyId()).ifPresent(store ->{
            store.accept();       
            repository().save(store);   
        });
        */

        // Store store = new Store();
        // store.setToyStatus("NOT AVAILABLE");
        // store.setRentalId(paid.getRentalId());        
        // /*
        // LOGIC GOES HERE
        // */
        // repository().save(store);


    }
    public static void rentalCancel(PayCancelled payCancelled){
        //setToyStatus("");
        Store store = new Store();
        store.setToyStatus("AVAILABLE");
        
        /*
        LOGIC GOES HERE
        */
        repository().save(store);


    }
    public static void toyReturn(ToyReturned toyReturned){

        Store store = new Store();
        store.setToyStatus("AVAILABLE");
        //store.setRentalId("");
        /*
        LOGIC GOES HERE
        */
        repository().save(store);


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
