package com.space.specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class ShipSpecificationBuilder {

    @Nullable
    public static Specification<Ship> getSpec(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating
    ) {
        ArrayList<ShipSpecification> shipSpecifications = new ArrayList<>();

        if (name != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("name", SearchOperation.CONTAINS, name)));
        if (planet != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("planet", SearchOperation.CONTAINS, planet)));
        if (shipType != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("shipType", SearchOperation.EQUALITY, shipType)));
        if (after != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("prodDate", SearchOperation.GREATER_THAN_OR_EQUAL, new Date(after))));
        if (before != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("prodDate", SearchOperation.LESS_THAN_OR_EQUAL, new Date(before))));
        if (isUsed != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("isUsed", SearchOperation.EQUALITY, isUsed)));
        if (minSpeed != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("speed", SearchOperation.GREATER_THAN_OR_EQUAL, minSpeed)));
        if (maxSpeed != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("speed", SearchOperation.LESS_THAN_OR_EQUAL, maxSpeed)));
        if (minCrewSize != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("crewSize", SearchOperation.GREATER_THAN_OR_EQUAL, minCrewSize)));
        if (maxCrewSize != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("crewSize", SearchOperation.LESS_THAN_OR_EQUAL, maxCrewSize)));
        if (minRating != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("rating", SearchOperation.GREATER_THAN_OR_EQUAL, minRating)));
        if (maxRating != null)
            shipSpecifications.add(new ShipSpecification(new SearchCriteria("rating", SearchOperation.LESS_THAN_OR_EQUAL, maxRating)));

        if (shipSpecifications.isEmpty())
            return null;

        Specification<Ship> spec = Specification.where(shipSpecifications.get(0));
        for (int i = 1; i < shipSpecifications.size(); i++) {
            spec = spec.and(shipSpecifications.get(i));
        }
        return spec;
    }
}
