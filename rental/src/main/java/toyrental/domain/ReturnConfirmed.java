package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;
 
@Data
public class ReturnConfirmed extends AbstractEvent {

    private Integer toyId;
    private Integer rentalId;
    private String name;
    private String toyStatus;
    private Integer toyRentalPrice;

    public ReturnConfirmed(){
        super();
    }
}
