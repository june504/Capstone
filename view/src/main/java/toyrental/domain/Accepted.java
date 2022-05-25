package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class Accepted extends AbstractEvent {

    private Integer toyId;
    private Integer rentalId;
    private String name;
    private Integer toyRentalPrice;
    private String toyStatus;

    public Accepted(){
        super();
    }
}
