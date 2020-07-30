package ru.sbrf.sb.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusIdSeq")
    private Long id;

    private String firstName;

    @ManyToMany(mappedBy = "employees")
    private List<Project> projects = new ArrayList<>();
}
