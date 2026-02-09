package com.example.todo.mapper;

import com.example.todo.entity.Todo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoMapper {
    List<Todo> selectByCondition(
            @Param("field") String field,
            @Param("keyword") String keyword,
            @Param("date") java.time.LocalDate date);
    long countByCondition(
            @Param("field") String field,
            @Param("keyword") String keyword,
            @Param("date") java.time.LocalDate date);
}
