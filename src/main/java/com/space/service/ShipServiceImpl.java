package com.space.service;

import com.space.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipDataService shipService;

    @Override
    public List<Ship> getAllShip() {
        return shipService.getAllShip();
    }

    @Override
    public int getShipsCount() {
        return shipService.getShipsCount();
    }

    @Override
    public Ship createShip(Ship ship) {
        return shipService.createShip(ship);
    }

    @Override
    public Ship getShip(int id) {
        return shipService.getShip(id);
    }

    @Override
    public Ship updateShip(Ship ship) {
        return shipService.updateShip(ship);
    }

    @Override
    public void deleteShip(Long id) {
        shipService.deleteShip(id);
    }
}
