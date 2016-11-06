package perftracker.domain.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Component;
import perftracker.domain.Criteria;
import perftracker.domain.TeamMember;

@Component
public class DomainMapper extends ObjectMapper {
    {
        addMixIn(Criteria.class, CriteriaDescriptor.class);
        //addMixIn(TeamMember.class, TeamMemberDescriptor.class);
    }

    @JsonDeserialize(as = CriteriaImpl.class)
    private static class CriteriaDescriptor extends CriteriaImpl {}
    @JsonDeserialize(as = TeamMemberImpl.class)
    private static class TeamMemberDescriptor extends TeamMemberImpl{}
}
