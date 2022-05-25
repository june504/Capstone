package toyrental.domain;

import toyrental.RepairApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import java.util.Date;
import java.util.concurrent.*;

@Entity
@Table(name="Repair_table")
@Data

public class Repair  {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Integer repairId;

    private Integer toyId;
    
    private String toyStatus;
    
    private String repairmanId;

    @PostPersist
    public void onPostPersist(){
        //RepairRequested repairRequested = new RepairRequested();
        //BeanUtils.copyProperties(this, repairRequested);
       // repairRequested.publishAfterCommit();
    }


    public static RepairRepository repository(){
        RepairRepository repairRepository = RepairApplication.applicationContext.getBean(RepairRepository.class);
        return repairRepository;
    }

    public void discard(){
        setToyStatus("DISCARDED");
        Discarded discarded = new Discarded();
        System.out.println("DISCARDED !!! ");
        
        /*
        Input Event Content
        */
        BeanUtils.copyProperties(this, discarded);
        discarded.publishAfterCommit();

    }
    public void repair(){
        setToyStatus("AVAILABLE"); 
        ToyRepaired toyRepaired = new ToyRepaired();     
        System.out.println("REPAIRED !!! ");
        BeanUtils.copyProperties(this, toyRepaired);
        toyRepaired.publishAfterCommit();

    }

    public static void requestRepair(RepairRequested repairRequested){

        Repair repair = new Repair();        
        repair.setToyId(repairRequested.getToyId());
        repair.setToyStatus(repairRequested.getToyStatus());        
        repair.setRepairmanId("홍길동");

        repository().save(repair);
        
                


    }


}
