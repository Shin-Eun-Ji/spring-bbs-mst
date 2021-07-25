package com.example.springbbsmst.domain.repository;

import com.example.springbbsmst.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByTitleContaining(String keyword);
    // JpaRepository<BoardEntity, Long> <======== Entity Class & PK
}
