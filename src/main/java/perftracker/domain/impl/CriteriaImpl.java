package perftracker.domain.impl;

import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;

class CriteriaImpl implements Criteria{

    private String name;
    private CriteriaType type;
    private int maxScore;

    CriteriaImpl() {}

    CriteriaImpl(String key, CriteriaType type, int maxScore) {
        this.name = key;
        this.type = type;
        this.maxScore = maxScore;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CriteriaType getType() {
        return type;
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }
}
