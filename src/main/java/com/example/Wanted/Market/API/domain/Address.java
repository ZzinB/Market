package com.example.Wanted.Market.API.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String street;
    private String city;
    private String state;
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
