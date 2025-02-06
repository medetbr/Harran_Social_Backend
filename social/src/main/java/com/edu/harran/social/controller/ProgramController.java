package com.edu.harran.social.controller;

import com.edu.harran.social.dto.programDto.ProgramRequestDto;
import com.edu.harran.social.dto.programDto.ProgramResponseDto;
import com.edu.harran.social.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/admin/programs/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("create")
    public void create(@RequestBody ProgramRequestDto programRequestDto) {
        programService.insert(programRequestDto);
    }

    @GetMapping("all")
    public List<ProgramResponseDto> getAll() {
        return programService.findAll();
    }

    @DeleteMapping("delete/{programId}")
    public void deleteByAcademicId(@PathVariable String programId) {
        programService.delete(programId);
    }

    @PatchMapping("update/{programId}")
    public void updateByProgramId(@PathVariable String programId, @RequestBody ProgramRequestDto programRequestDto) {
        programService.update(programId, programRequestDto);
    }
}
