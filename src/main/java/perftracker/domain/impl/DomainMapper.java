package perftracker.domain.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;

@Component
public class DomainMapper extends ObjectMapper {
    {
        addMixIn(Criteria.class, CriteriaDescriptor.class);
    }

    @JsonDeserialize(as = CriteriaImpl.class)
    private static class CriteriaDescriptor extends CriteriaImpl {}
}
