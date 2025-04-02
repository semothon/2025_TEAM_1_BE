package _2024.winter.semoton.domain.landmark.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "landmarks")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Landmark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 랜드마크 이름 (정문, 운동장 등)

    @Column(nullable = false)
    private double latitude; // 위도

    @Column(nullable = false)
    private double longitude; // 경도

    @Builder
    public Landmark(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
