package com.study.sos_backend.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Locate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COORDINATE_ID", nullable = false)
    private Long id;

    private Double latitude;

    private Double longitude;

    public Locate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}