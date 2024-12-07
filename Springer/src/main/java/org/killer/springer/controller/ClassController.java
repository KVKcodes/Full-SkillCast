package org.killer.springer.controller;

import java.util.List;

import org.killer.springer.entity.Class;
import org.killer.springer.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getClassById(@PathVariable Integer id) {
        Class classObj = classService.getClassById(id);
        if (classObj != null) {
            return ResponseEntity.ok(classObj);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Class> createClass(@RequestBody Class classObj) {
        return ResponseEntity.ok(classService.createClass(classObj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Class> updateClass(@PathVariable Integer id, @RequestBody Class classObj) {
        Class updatedClass = classService.updateClass(id, classObj);
        if (updatedClass != null) {
            return ResponseEntity.ok(updatedClass);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Integer id) {
        if (classService.deleteClass(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 