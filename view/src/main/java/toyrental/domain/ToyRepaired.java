package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class ToyRepaired extends AbstractEvent {

    private String toyStatus;
    private Integer toyId;
    private Integer repairId;
    private String repairmanId;

}

