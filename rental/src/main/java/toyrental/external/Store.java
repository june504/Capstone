package toyrental.external;

import lombok.Data;
import java.util.Date;

@Data
public class Store { 

    private Integer toyId;      
    private Integer rentalId;    
    private String name;     
    private Integer toyRentalPrice;      
    private String toyStatus;
 
}
