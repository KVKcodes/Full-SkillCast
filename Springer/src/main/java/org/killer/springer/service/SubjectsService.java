package org.killer.springer.service;

import java.util.List;

import org.killer.springer.entity.Subjects;
import org.killer.springer.repository.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectsService {

    @Autowired
    private SubjectsRepository subjectsRepository;

    public List<Subjects> getAllSubjects() {
        return subjectsRepository.findAll();
    }

    public Subjects getSubjectById(Integer id) {
        return subjectsRepository.findById(id).orElse(null);
    }

    public Subjects createSubject(Subjects subject) {
        return subjectsRepository.save(subject);
    }

    public Subjects updateSubject(Integer id, Subjects subject) {
        if (subjectsRepository.existsById(id)) {
            subject.setId(id);
            return subjectsRepository.save(subject);
        }
        return null;
    }

    public boolean deleteSubject(Integer id) {
        if (subjectsRepository.existsById(id)) {
            subjectsRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 