package perftracker.domain;

public interface Criteria {

    String getName();

    CriteriaType getType();

    int getMaxScore();
}
