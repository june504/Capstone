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

    @PrePersist
    public void validate(){
        //repository().findByRentalId(getRentalId()).ifPresent(store -> {
        //    throw new RuntimeException("PLEASE CHECK RENTALID");
        //});

    }    
    
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
    
    public void accept(){
        setToyStatus("ACCEPTED");
        Accepted accepted = new Accepted();
        BeanUtils.copyProperties(this, accepted);
        accepted.publishAfterCommit();
    }    
    
    public void returnConfirm(){
        setToyStatus("AVAILABLE");
        ReturnConfirmed returnConfirmed = new ReturnConfirmed();
        BeanUtils.copyProperties(this, returnConfirmed);
        returnConfirmed.publishAfterCommit();
    }

    public static void rentalConfirm(Paid paid){
        try{
            Integer toyId = paid.getToyId();
            Optional<Store> optionalStore = repository().findByToyId(toyId);
            optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
            Store store = optionalStore.get();
            /*
            Accepted accepted = new Accepted();            
            accepted.setRentalId(paid.getRentalId());
            accepted.setToyRentalPrice(paid.getToyRentalPrice());
            accepted.setToyId(paid.getToyId());
            accepted.setToyStatus("ACCEPTED");
            
            accepted.publish();
            */
            store.setRentalId(paid.getRentalId());
            store.setToyStatus("RENTAL REQUESTED");
            repository().save(store);

        }catch(Exception e){
            throw new RuntimeException(e);
        } 
    }

    public static void rentalCancel(PayCancelled payCancelled){
        try{
            Integer rentalId = payCancelled.getRentalId();
            Optional<Store> optionalStore = repository().findByRentalId(rentalId);
            optionalStore.orElseThrow(()-> new Exception("No Entity Found"));

            Store store = optionalStore.get();
            store.setToyStatus("AVAILABLE");
            repository().save(store);
        }  catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    
    public static void toyReturn(ToyReturned toyReturned){
        Integer rentalId = toyReturned.getRentalId();
        repository().findByRentalId(rentalId).ifPresent(store->{
            //Store store = optionalStore.get();
            store.setToyStatus("RETURNED");
            repository().save(store);
        });
    }

    public static void updateToyStatus(ToyRepaired toyRepaired){
        Integer toyId = toyRepaired.getToyId();
        repository().findByToyId(toyId).ifPresent(store->{            
            store.setToyStatus(toyRepaired.getToyStatus());
            repository().save(store);
        });
    }

    public static void updateToyStatus(Discarded discarded){   
        Integer toyId = discarded.getToyId();
        repository().findByToyId(toyId).ifPresent(store->{            
            store.setToyStatus(discarded.getToyStatus());
            repository().save(store);
        });
    }
}
