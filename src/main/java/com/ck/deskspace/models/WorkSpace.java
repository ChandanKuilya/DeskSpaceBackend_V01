package com.ck.deskspace.models;
import com.ck.deskspace.models.enums.WorkSpaceType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workspaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WorkSpaceType type;

    private Integer capacity;

    private BigDecimal pricePerHour;

    // THE MANY-TO-MANY MAPPING
    // This creates a third table called 'workspace_amenities'
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "workspace_amenities",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )

    private Set<Amenity> amenities = new HashSet<>();
}