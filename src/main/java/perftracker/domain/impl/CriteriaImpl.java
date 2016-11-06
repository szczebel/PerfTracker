package perftracker.domain.impl;

import perftracker.domain.Criteria;
import perftracker.domain.CriteriaType;

class CriteriaImpl implements Criteria{

    private String name;
    private CriteriaType type;
    private int maxGrade;

    CriteriaImpl() {}

    CriteriaImpl(String key, CriteriaType type, int maxGrade) {
        this.name = key;
        this.type = type;
        this.maxGrade = maxGrade;
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
    public int getMaxGrade() {
        return maxGrade;
    }
}
