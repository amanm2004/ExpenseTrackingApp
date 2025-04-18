package org.Auth.entities;

import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table()
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private long roleId;

    private String name;
}
