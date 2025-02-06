package com.edu.harran.social.service;

import com.edu.harran.social.dto.academicDto.AcademicResponseDto;
import com.edu.harran.social.dto.programDto.ProgramRequestDto;
import com.edu.harran.social.dto.programDto.ProgramResponseDto;
import com.edu.harran.social.entity.Academic;
import com.edu.harran.social.entity.Program;
import com.edu.harran.social.exception.academic.AcademicCreateException;
import com.edu.harran.social.exception.crudException.InvalidIdException;
import com.edu.harran.social.exception.responseException.InternalServerException;
import com.edu.harran.social.repository.AcademicRepository;
import com.edu.harran.social.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final AcademicRepository academicRepository;
    private final ModelMapper modelMapper;

    public void insert(ProgramRequestDto programRequestDto) {
        try {
            Program findProgramByName = programRepository.findByName(programRequestDto.getName());
            if(findProgramByName!=null)throw new InternalServerException(programRequestDto.getName()+" zaten mevcut");
            Academic academic = academicRepository.findByAcademicId(programRequestDto.getAcademic_id());
            if(academic==null){
                throw new InvalidIdException("Geçersiz id");
            }else{
                Program program = new Program();
                program.setName(programRequestDto.getName());
                program.setE_mail(programRequestDto.getE_mail());
                program.setAcademic(academic);
                programRepository.save(program);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProgramResponseDto> findAll() {
        List<ProgramResponseDto> programResponseDtoList = new ArrayList<>();
        List<Program> programs = programRepository.findAll();
        for (Program program : programs) {
            programResponseDtoList.add(modelMapper.map(program, ProgramResponseDto.class));
        }
        return programResponseDtoList;
    }

    public void delete(String programId) {
        try {
            Program program = programRepository.findByProgramId(programId);
            if(program==null || program.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            program.setIsDeleted(true);
            programRepository.save(program);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String programId, ProgramRequestDto programRequestDto) {
        try {
            Program program = programRepository.findByProgramId(programId);
            if(program==null || program.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            if(!isNullOrEmpty(programRequestDto.getName())){
                Academic academic = academicRepository.findByAcademicId(programRequestDto.getAcademic_id());
                program.setName(programRequestDto.getName());
                program.setE_mail(programRequestDto.getE_mail());
                program.setAcademic(academic);
                programRepository.save(program);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
