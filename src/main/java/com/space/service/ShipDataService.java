package com.space.service;

import com.space.repository.ShipCrudRepository;
import com.space.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ShipDataService {

    @Autowired
    private ShipCrudRepository repository;

    @Transactional
    public List<Ship> getAllShip(Specification<Ship> spec) {
        return new ArrayList<>(repository.findAll(spec));
    }
    public List<Ship> getAllShip() {
        return getAllShip(null);
    }
    @Transactional
    public int getShipsCount(Specification<Ship> spec) {
        return (int) repository.count(spec);
    }
    public int getShipsCount() {
        return getShipsCount(null);
    }
    @Transactional
    public Ship createShip(Ship ship) {
        return repository.save(calculateShipRating(ship, ship.isUsed));
    }
    @Transactional
    public Ship getShip(int id) {
        return repository.findById((long) id).orElse(null);
    }
    @Transactional
    public Ship updateShip(Ship ship) {
        return repository.save(calculateShipRating(ship, ship.isUsed));
    }
    @Transactional
    public void deleteShip(long id) {
        repository.deleteById(id);
    }

    private Ship calculateShipRating(Ship ship, boolean isUsed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.prodDate);
        double koef = 1.0;
        if (isUsed) koef = 0.5;
        ship.rating = new BigDecimal((80 * ship.speed * koef) / (3019 - calendar.get(Calendar.YEAR) + 1)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return ship;
    }

}
