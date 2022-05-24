package toyrental.infra;
import toyrental.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class RentalHateoasProcessor implements RepresentationModelProcessor<EntityModel<Rental>>  {

    @Override
    public EntityModel<Rental> process(EntityModel<Rental> model) {
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/return").withRel("return"));
        
        return model;
    }
    
}

