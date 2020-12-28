package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class ShipSpecification implements Specification<Ship> {

    private SearchCriteria criteria;

    public ShipSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                if (criteria.getKey().toLowerCase().contains("date"))
                    return builder.greaterThan(root.get(criteria.getKey()), (Date) criteria.getValue());
                else
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case GREATER_THAN_OR_EQUAL:
                if (criteria.getKey().toLowerCase().contains("date"))
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                else
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                if (criteria.getKey().toLowerCase().contains("date"))
                    return builder.lessThan(root.get(criteria.getKey()), (Date) criteria.getValue());
                else
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN_OR_EQUAL:
                if (criteria.getKey().toLowerCase().contains("date"))
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                else
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(
                        criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
