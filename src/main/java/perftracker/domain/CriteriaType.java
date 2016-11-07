package perftracker.domain;

public enum CriteriaType {
    HARDSKILL{
        @Override
        public String toString() {
            return "Hard skill";
        }
    }, SOFTSKILL {
        @Override
        public String toString() {
            return "Soft skill";
        }
    }
}
