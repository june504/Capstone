package toyrental.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name="ToyList_table")
@Data
public class ToyList {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String name;
        private Integer rentalId;
        private String status;
        private String repairmanId;


}