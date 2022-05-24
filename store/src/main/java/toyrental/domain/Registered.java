package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class Registered extends AbstractEvent {

    private Integer toyId;
    private String name;
    private Long toyRentalPrice;
    private String toyStatus;
    private String rentalId;

    public Registered(){
        super();
    }
}
