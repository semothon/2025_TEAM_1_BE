package _2024.winter.semoton.domain.progress.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Grade {
    A("A"), A_PLUS("A+"), A_MINUS("A-"),
    B("B"), B_PLUS("B+"), B_MINUS("B-"),
    C("C"), C_PLUS("C+"), C_MINUS("C-"),
    F("F");

    private final String label;

    Grade(String label) {
        this.label = label;
    }
}
