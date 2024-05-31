package com.example.gticslab720212607.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "resources")
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resourceid")
    private Integer id;

    @Column(name = "name")
    private String name;

}
