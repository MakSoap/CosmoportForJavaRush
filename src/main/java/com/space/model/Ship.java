package com.space.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
@FilterDef(name="shipFilter", parameters={
        @ParamDef( name="name", type="string" ),
        @ParamDef( name="planet", type="string" ),
        @ParamDef( name="after", type="date" ),
        @ParamDef( name="before", type="date" ),
        @ParamDef( name="isUsed", type="boolean" ),
        @ParamDef( name="minSpeed", type="double"),
        @ParamDef( name="maxSpeed", type="double"),
        @ParamDef( name="minCrewSize", type="integer"),
        @ParamDef( name="maxCrewSize", type="integer"),
        @ParamDef( name="minRating", type="double"),
        @ParamDef( name="maxRating", type="double"),
})
@Filters( {
        @Filter(name="shipFilter", condition="name like '%:name%'"),
        @Filter(name="shipFilter", condition="planet like '%:planet%'"),
        @Filter(name="shipFilter", condition="prodDate >= :after"),
        @Filter(name="shipFilter", condition="prodDate < :before"),
} )
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "planet")
    public String planet;
    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    public ShipType shipType;
    @Column(name = "prodDate")
    public Date prodDate;
    @Column(name = "isUsed")
    public Boolean isUsed;
    @Column(name = "speed")
    public Double speed;
    @Column(name = "crewSize")
    public Integer crewSize;
    @Column(name = "rating")
    public Double rating;
}
