package org.killer.springer.service;

import java.util.List;

import org.killer.springer.entity.Class;
import org.killer.springer.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    public Class getClassById(Integer id) {
        return classRepository.findById(id).orElse(null);
    }

    public Class createClass(Class classObj) {
        return classRepository.save(classObj);
    }

    public Class updateClass(Integer id, Class classObj) {
        if (classRepository.existsById(id)) {
            classObj.setId(id);
            return classRepository.save(classObj);
        }
        return null;
    }

    public boolean deleteClass(Integer id) {
        if (classRepository.existsById(id)) {
            classRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 