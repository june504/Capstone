package toyrental.common;


import toyrental.RentalApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { RentalApplication.class })
public class CucumberSpingConfiguration {
    
}
