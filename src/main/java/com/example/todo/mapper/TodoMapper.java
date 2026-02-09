package com.example.todo.mapper;

import com.example.todo.entity.Todo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoMapper {
    List<Todo> selectByFilters(
            @Param("type") String type,
            @Param("priority") Integer priority);
    long countByFilters(
            @Param("type") String type,
            @Param("priority") Integer priority);
}
