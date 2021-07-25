package com.example.springbbsmst.domain.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter //lombok
@Entity
@Table(name="board")
public class BoardEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String writer;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public BoardEntity() {
    }

    @Builder //lombok
    public BoardEntity(Long id, String writer, String title, String content) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}
