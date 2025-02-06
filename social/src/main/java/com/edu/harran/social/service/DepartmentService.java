package com.edu.harran.social.service;

import com.edu.harran.social.dto.DepartmentDto.DepartmentRequestDto;
import com.edu.harran.social.dto.DepartmentDto.DepartmentResponseDto;
import com.edu.harran.social.entity.Academic;
import com.edu.harran.social.entity.Department;
import com.edu.harran.social.entity.Program;
import com.edu.harran.social.exception.crudException.InvalidIdException;
import com.edu.harran.social.exception.responseException.InternalServerException;
import com.edu.harran.social.repository.DepartmentRepository;
import com.edu.harran.social.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ProgramRepository programRepository;
    private final ModelMapper modelMapper;
    public void insert(DepartmentRequestDto departmentRequest) {
        try {
            Department findDepartment = departmentRepository.findByName(departmentRequest.getName());
            if(findDepartment!=null)throw new InternalServerException(departmentRequest.getName()+" zaten mevcut");
            Program program = programRepository.findByProgramId(departmentRequest.getProgram_id());
            if(program==null){
                throw new InvalidIdException("Geçersiz id");
            }else{
                Department department = new Department();
                department.setName(departmentRequest.getName());
                department.setE_mail(departmentRequest.getE_mail());
                department.setProgram(program);
                departmentRepository.save(department);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<DepartmentResponseDto> findAll() {
        List<DepartmentResponseDto> departmentResponseDtoList = new ArrayList<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            departmentResponseDtoList.add(modelMapper.map(department, DepartmentResponseDto.class));
        }
        return departmentResponseDtoList;
    }

    public void delete(String departmentId) {
        try {
            Department department = departmentRepository.findByDepartmentId(departmentId);
            if(department==null || department.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            department.setIsDeleted(true);
            departmentRepository.save(department);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String departmentId, DepartmentRequestDto departmentRequestDto) {
        try {
            Department department = departmentRepository.findByDepartmentId(departmentId);
            if(department==null || department.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            if(!isNullOrEmpty(departmentRequestDto.getName())){
                Program program = programRepository.findByProgramId(departmentRequestDto.getProgram_id());
                department.setName(departmentRequestDto.getName());
                department.setE_mail(departmentRequestDto.getE_mail());
                if (!isNullOrEmpty(departmentRequestDto.getProgram_id())) {
                    department.setProgram(program);
                }
                departmentRepository.save(department);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public Department findByDepartmenId(String departmentId) {
        return departmentRepository.findByDepartmentId(departmentId);
    }
}
