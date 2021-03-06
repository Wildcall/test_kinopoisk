package ru.malygin.parser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Column(name = "title", nullable = false)
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

    public boolean hasError() {
        return pos == -1
                || rating == -1.0
                || title.equals("-1")
                || prodYear == -1
                || voteCount == -1;
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
        return "Id: " + id + " / Pos: " + pos + " / Rating: " + rating + " / Title: " + title + " / Year: " + prodYear + " / Vote: " + voteCount + " / Save date: " + insertDate;
    }
}
