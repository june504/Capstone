package toyrental.external;

import lombok.Data;
import java.util.Date;


@Data
public class Payment {

    private Integer payId;
    private Integer rentalId;
    private String payStatus;
    private Integer toyRentalPrice;
    private Integer toyId; 
 
}
