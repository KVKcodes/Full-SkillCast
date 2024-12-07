package org.killer.springer.controller;

import java.util.List;

import org.killer.springer.entity.Attendance;
import org.killer.springer.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/{studentId}/{classId}/join")
    public ResponseEntity<Attendance> markAttendance(
            @PathVariable Integer studentId,
            @PathVariable Integer classId) {
        return ResponseEntity.ok(attendanceService.markAttendance(studentId, classId));
    }

    @PutMapping("/{studentId}/{classId}/leave")
    public ResponseEntity<Attendance> markLeave(
            @PathVariable Integer studentId,
            @PathVariable Integer classId) {
        return ResponseEntity.ok(attendanceService.updateLeaveTime(studentId, classId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(@PathVariable Integer studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studentId));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Attendance>> getClassAttendance(@PathVariable Integer classId) {
        return ResponseEntity.ok(attendanceService.getClassAttendance(classId));
    }
} 