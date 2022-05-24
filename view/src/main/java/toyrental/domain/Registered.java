package toyrental.domain;

import toyrental.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;

@Data
public class Registered extends AbstractEvent {

    private Integer toyId;
    private String name;
    private Long toyRentalPrice;
    private String toyStatus;
    private String rentalId;
}
