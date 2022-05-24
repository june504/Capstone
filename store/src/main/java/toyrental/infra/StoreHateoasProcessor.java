package toyrental.infra;
import toyrental.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class StoreHateoasProcessor implements RepresentationModelProcessor<EntityModel<Store>>  {

    @Override
    public EntityModel<Store> process(EntityModel<Store> model) {
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/repairrequest").withRel("repairrequest"));
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/accept").withRel("accept"));
        model.add(Link.of(model.getRequiredLink("self").getHref() + "/returnconfirm").withRel("returnconfirm"));
        
        return model;
    }
    
}

