package com.smart.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String name;
    private String secondName;
    private String email;
    private String phone;
    private String work;
    private String profileImage;
    @Column(length = 1000)
    private String description;

//    @ManyToOne
//    private User user;

}
