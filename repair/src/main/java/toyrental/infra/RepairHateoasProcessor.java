package toyrental.infra;
import toyrental.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class RepairHateoasProcessor implements RepresentationModelProcessor<EntityModel<Repair>>  {

    @Override
    public EntityModel<Repair> process(EntityModel<Repair> model) {
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/discard").withRel("discard"));
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/repair").withRel("repair"));
        
        return model;
    }
    
}

