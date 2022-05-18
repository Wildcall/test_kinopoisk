package ru.malygin.parser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "films", schema = "public", catalog = "kinopoisk_data")
public class Film {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "pos", nullable = false)
    private Integer pos;

    @Basic
    @Column(name = "rating", nullable = false)
    private Double rating;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "prod_year", nullable = false)
    private Integer prodYear;

    @Basic
    @Column(name = "vote_count", nullable = false)
    private Integer voteCount;

    @Basic
    @Column(name = "insert_date", nullable = false)
    private Date insertDate;

    public Film(Integer pos,
                Double rating,
                String title,
                Integer prodYear,
                Integer voteCount) {
        this.pos = pos;
        this.rating = rating;
        this.title = title;
        this.prodYear = prodYear;
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Film that = (Film) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Номер: " + id +
                " / Позиция: " + pos +
                " / Рейтинг: " + rating +
                " / Название: " + title +
                " / Год выпуска: " + prodYear +
                " / Кол-во голосов: " + voteCount +
                " / Дата сохранения: " + insertDate;
    }
}
