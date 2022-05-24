package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class RepairRequested extends AbstractEvent {

    private Integer toyId;
    private String name;
    private String toyStatus;

}

