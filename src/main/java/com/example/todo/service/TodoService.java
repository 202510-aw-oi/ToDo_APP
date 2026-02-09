package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.form.TodoForm;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.repository.TodoRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    public void save(TodoForm form) {
        Todo todo = new Todo();
        todo.setAuthor(form.getAuthor());
        todo.setTitle(form.getTitle());
        todo.setDetail(form.getDetail());
        todo.setDescription(form.getDescription());
        todo.setType(form.getType());
        todo.setPriority(form.getPriority());
        todo.setDueDate(form.getDueDate());
        todoRepository.save(todo);
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Todo> searchByCondition(String field, String keyword) {
        String normalizedField = normalizeField(field);
        String normalizedKeyword = normalizeKeyword(keyword);
        LocalDate date = parseDateIfNeeded(normalizedField, normalizedKeyword);
        if (isDateField(normalizedField) && normalizedKeyword != null && date == null) {
            return List.of();
        }
        return todoMapper.selectByCondition(normalizedField, normalizedKeyword, date);
    }

    public long countByCondition(String field, String keyword) {
        String normalizedField = normalizeField(field);
        String normalizedKeyword = normalizeKeyword(keyword);
        LocalDate date = parseDateIfNeeded(normalizedField, normalizedKeyword);
        if (isDateField(normalizedField) && normalizedKeyword != null && date == null) {
            return 0;
        }
        return todoMapper.countByCondition(normalizedField, normalizedKeyword, date);
    }

    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public void update(TodoForm form) {
        Todo todo = todoRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + form.getId()));
        todo.setAuthor(form.getAuthor());
        todo.setTitle(form.getTitle());
        todo.setDetail(form.getDetail());
        todo.setDescription(form.getDescription());
        todo.setType(form.getType());
        todo.setPriority(form.getPriority());
        todo.setDueDate(form.getDueDate());
        todoRepository.save(todo);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeField(String field) {
        if (field == null) {
            return null;
        }
        String trimmed = field.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isDateField(String field) {
        return "createdAt".equals(field) || "dueDate".equals(field);
    }

    private LocalDate parseDateIfNeeded(String field, String keyword) {
        if (!isDateField(field) || keyword == null) {
            return null;
        }
        try {
            return LocalDate.parse(keyword);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
