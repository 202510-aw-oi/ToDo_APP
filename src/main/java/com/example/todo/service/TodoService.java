package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.form.TodoForm;
import com.example.todo.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
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
}
