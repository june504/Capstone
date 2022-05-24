package toyrental.common;


import toyrental.RepairApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { RepairApplication.class })
public class CucumberSpingConfiguration {
    
}
