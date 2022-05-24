package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class PayCancelled extends AbstractEvent {

    private Integer payId;
    private Integer rentalId;
    private String payStatus;
    private Integer toyRentalPrice;

}

