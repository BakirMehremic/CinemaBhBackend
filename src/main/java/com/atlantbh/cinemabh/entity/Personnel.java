package com.atlantbh.cinemabh.entity;

import com.atlantbh.cinemabh.constant.PersonnelConstants;
import com.atlantbh.cinemabh.enums.PersonnelType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "personnel", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}))
public class Personnel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, length = PersonnelConstants.PERSONNEL_NAME_MAX_LENGTH)
  @Setter(AccessLevel.NONE)
  private String name;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  @Setter(AccessLevel.NONE)
  private PersonnelType personnelType;
}
