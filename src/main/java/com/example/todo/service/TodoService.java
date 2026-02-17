package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.form.TodoForm;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.repository.TodoRepository;
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
        todo.setCountry(form.getCountry());
        todo.setPriority(form.getPriority());
        todo.setDueDate(form.getDueDate());
        todo.setAttachmentUrl(form.getAttachmentUrl());
        todoRepository.save(todo);
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Todo> searchByFilters(String type, String priorityText, String sort) {
        String normalizedType = normalizeText(type);
        Integer priority = parsePriority(priorityText);
        if (priorityText != null && !priorityText.trim().isEmpty() && priority == null) {
            return List.of();
        }
        String normalizedSort = normalizeSort(sort);
        return todoMapper.selectByFilters(normalizedType, priority, normalizedSort);
    }

    public long countByFilters(String type, String priorityText) {
        String normalizedType = normalizeText(type);
        Integer priority = parsePriority(priorityText);
        if (priorityText != null && !priorityText.trim().isEmpty() && priority == null) {
            return 0;
        }
        return todoMapper.countByFilters(normalizedType, priority);
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
        todo.setCountry(form.getCountry());
        todo.setPriority(form.getPriority());
        todo.setDueDate(form.getDueDate());
        todo.setAttachmentUrl(form.getAttachmentUrl());
        todoRepository.save(todo);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Integer parsePriority(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String normalizeSort(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if ("due_asc".equals(trimmed) || "due_desc".equals(trimmed)) {
            return trimmed;
        }
        return null;
    }
}
