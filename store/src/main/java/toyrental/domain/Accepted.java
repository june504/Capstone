package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class Accepted extends AbstractEvent {

    private Integer toyId;
    private String rentalId;
    private String name;
    private Long toyRentalPrice;
    private String toyStatus;

    public Accepted(){
        super();
    }
}
