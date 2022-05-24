package toyrental.domain;

import toyrental.domain.*;
import toyrental.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class Discarded extends AbstractEvent {

    private String toyStatus;
    private Integer toyId;
    private Integer repairId;
    private String repairmanId;

    public Discarded(){
        super();
    }
}
