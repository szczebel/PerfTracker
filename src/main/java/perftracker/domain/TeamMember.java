package perftracker.domain;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface TeamMember {

    String getName();

    Map<Criteria, Integer> getScores();

    int getTotalScore(CriteriaType type);

    void whenScoreChanged(BiConsumer<Criteria, Integer> listener);
}
