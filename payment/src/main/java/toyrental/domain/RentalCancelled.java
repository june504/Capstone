package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class RentalCancelled extends AbstractEvent {

    private Integer rentalId;
    private Integer customerId;
    private Integer toyId;
    private String rentalStatus;

}

