package perftracker.domain;

import java.util.Map;
import java.util.function.BiConsumer;

public interface TeamMember {

    String getName();

    Map<String, Integer> getGrades();

    void whenGradeChanged(BiConsumer<String, Integer> listener);


}
