package com.edu.harran.social.controller;

import com.edu.harran.social.dto.academicPersonel.AcademicPersonelRequestDto;
import com.edu.harran.social.dto.academicPersonel.AcademicPersonelResponseDto;
import com.edu.harran.social.service.AcademicPersonelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/academic-personel/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AcademicPersonelController {

    private final AcademicPersonelService academicPersonelService;

    @PostMapping("admin/create")
    public void create(@RequestBody AcademicPersonelRequestDto academicPersonelRequestDto) {
        academicPersonelService.insert(academicPersonelRequestDto);
    }

    @GetMapping("admin/all")
    public List<AcademicPersonelResponseDto> getAll() {
        return academicPersonelService.findAll();
    }

    @GetMapping("{departmentId}")
    public List<AcademicPersonelResponseDto> getByDepartmentId(@PathVariable String departmentId,@RequestHeader("Authorization") String token) {
        return academicPersonelService.findByDepartmentId(departmentId);
    }
    @GetMapping("search")
    public List<AcademicPersonelResponseDto> getByQueryOfName(@RequestParam("name") String query,@RequestHeader("Authorization") String token) {
        if (query == null || query.trim().isEmpty()) {
            return null;
        }else return academicPersonelService.findByQueryOfName(query);
    }

    @DeleteMapping("admin/delete/{academicPersonelId}")
    public void deleteByAcademicPersonelId(@PathVariable String academicPersonelId) {
        academicPersonelService.delete(academicPersonelId);
    }

    @PatchMapping("admin/update/{academicPersonelId}")
    public void updateByAcademicPersonelId(@PathVariable String academicPersonelId, @RequestBody AcademicPersonelRequestDto academicPersonelRequestDto) {
        academicPersonelService.update(academicPersonelId, academicPersonelRequestDto);
    }
}
