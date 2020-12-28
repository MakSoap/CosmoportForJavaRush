package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipCrudRepository extends CrudRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {

}
