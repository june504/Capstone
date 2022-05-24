package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class RepairRequested extends AbstractEvent {

    private Integer toyId;
    private String name;
    private String toyStatus;

    public RepairRequested(){
        super();
    }
}
