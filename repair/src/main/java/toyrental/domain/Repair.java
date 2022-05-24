package toyrental.domain;

import toyrental.RepairApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Repair_table")
@Data

public class Repair  {

    
    
    private String toyStatus;
    
    
    private Integer toyId;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    private Integer repairId;
    
    
    private String repairmanId;

    @PostPersist
    public void onPostPersist(){
    }


    public static RepairRepository repository(){
        RepairRepository repairRepository = RepairApplication.applicationContext.getBean(RepairRepository.class);
        return repairRepository;
    }

    public static void discard(){
        Discarded discarded = new Discarded();
        /*
        Input Event Content
        */
        discarded.publishAfterCommit();

    }
    public static void repair(){
        ToyRepaired toyRepaired = new ToyRepaired();
        /*
        Input Event Content
        */
        toyRepaired.publishAfterCommit();

    }

    public static void requestRepair(RepairRequested repairRequested){

        Repair repair = new Repair();
        /*
        LOGIC GOES HERE
        */
        // repository().save(repair);


    }


}
