package com.example.lab2.dao;

import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.BookType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 从数据库查询书籍信息的dao层
 */
public interface BookTypeRepository extends JpaRepository<BookType, Long> {





}
