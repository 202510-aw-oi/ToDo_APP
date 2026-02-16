package com.example.todo.controller;

import com.example.todo.form.TodoForm;
import com.example.todo.service.TodoService;
import com.example.todo.entity.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "priority", required = false) String priority,
            Model model) {
        List<Todo> todos = todoService.searchByFilters(type, priority);
        long count = todoService.countByFilters(type, priority);
        model.addAttribute("todos", todos);
        model.addAttribute("type", type == null ? "" : type);
        model.addAttribute("priority", priority == null ? "" : priority);
        model.addAttribute("count", count);
        model.addAttribute("isSearching",
                (type != null && !type.trim().isEmpty())
                        || (priority != null && !priority.trim().isEmpty()));
        return "index";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("todoForm", new TodoForm());
        return "edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Optional<Todo> todoOpt = todoService.findById(id);
        if (todoOpt.isEmpty()) {
            return "redirect:/";
        }
        Todo todo = todoOpt.get();
        TodoForm form = new TodoForm();
        form.setId(todo.getId());
        form.setAuthor(todo.getAuthor());
        form.setTitle(todo.getTitle());
        form.setDetail(todo.getDetail());
        form.setDescription(todo.getDescription());
        form.setType(todo.getType());
        form.setPriority(todo.getPriority());
        form.setDueDate(todo.getDueDate());
        form.setAttachmentUrl(todo.getAttachmentUrl());
        model.addAttribute("todoForm", form);
        return "edit";
    }

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute TodoForm todoForm, Model model) {
        model.addAttribute("todoForm", todoForm);
        return "confirm";
    }

    @PostMapping("/complete")
    public String complete(@ModelAttribute TodoForm todoForm, RedirectAttributes redirectAttributes) {
        if (todoForm.getId() == null) {
            todoService.save(todoForm);
        } else {
            todoService.update(todoForm);
        }
        redirectAttributes.addFlashAttribute("todoForm", todoForm);
        return "redirect:/complete";
    }

    @GetMapping("/complete")
    public String completeView(@ModelAttribute("todoForm") TodoForm todoForm) {
        return "complete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        todoService.delete(id);
        return "redirect:/";
    }
}
