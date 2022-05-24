package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class PayCancelled extends AbstractEvent {

    private Integer payId;
    private Integer rentalId;
    private String payStatus;
    private Integer toyRentalPrice;

    public PayCancelled(){
        super();
    }
}
