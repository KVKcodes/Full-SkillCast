package org.killer.springer.controller;

import java.util.List;

import org.killer.springer.entity.Subjects;
import org.killer.springer.service.SubjectsService;
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
@RequestMapping("/api/subjects")
public class SubjectsController {

    @Autowired
    private SubjectsService subjectsService;

    @GetMapping
    public ResponseEntity<List<Subjects>> getAllSubjects() {
        return ResponseEntity.ok(subjectsService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subjects> getSubjectById(@PathVariable Integer id) {
        Subjects subject = subjectsService.getSubjectById(id);
        if (subject != null) {
            return ResponseEntity.ok(subject);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Subjects> createSubject(@RequestBody Subjects subject) {
        return ResponseEntity.ok(subjectsService.createSubject(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subjects> updateSubject(@PathVariable Integer id, @RequestBody Subjects subject) {
        Subjects updatedSubject = subjectsService.updateSubject(id, subject);
        if (updatedSubject != null) {
            return ResponseEntity.ok(updatedSubject);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Integer id) {
        if (subjectsService.deleteSubject(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 