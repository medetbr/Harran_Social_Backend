package com.edu.harran.social.service;

import com.edu.harran.social.dto.academicPersonel.AcademicPersonelRequestDto;
import com.edu.harran.social.dto.academicPersonel.AcademicPersonelResponseDto;
import com.edu.harran.social.entity.AcademicPersonel;
import com.edu.harran.social.entity.Department;
import com.edu.harran.social.exception.crudException.InvalidIdException;
import com.edu.harran.social.repository.AcademicPersonelRepository;
import com.edu.harran.social.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicPersonelService {

    private final AcademicPersonelRepository academicPersonelRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public void insert(AcademicPersonelRequestDto academicPersonelRequestDto) {
        try {
            Department department = departmentRepository.findByDepartmentId(academicPersonelRequestDto.getDepartment_id());
            if (department == null) {
                throw new InvalidIdException("Geçersiz id");
            } else {
                AcademicPersonel academicPersonel = new AcademicPersonel();
                academicPersonel.setName(academicPersonelRequestDto.getName());
                academicPersonel.setE_mail(academicPersonelRequestDto.getE_mail());
                academicPersonel.setDepartment(department);
                academicPersonelRepository.save(academicPersonel);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AcademicPersonelResponseDto> findAll() {
        List<AcademicPersonelResponseDto> academicPersonelResponseDtoList = new ArrayList<>();
        List<AcademicPersonel> academicPersonels = academicPersonelRepository.findAll();
        for (AcademicPersonel academicPersonel : academicPersonels) {
            academicPersonelResponseDtoList.add(modelMapper.map(academicPersonel, AcademicPersonelResponseDto.class));
        }
        return academicPersonelResponseDtoList;
    }

    public void delete(String academicPersonelId) {
        try {
            AcademicPersonel academicPersonel = academicPersonelRepository.findByAcademicPersonelId(academicPersonelId);
            if (academicPersonel == null || academicPersonel.getIsDeleted())
                throw new InvalidIdException("Geçersiz id");
            academicPersonel.setIsDeleted(true);
            academicPersonelRepository.save(academicPersonel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String academicPersonelId, AcademicPersonelRequestDto academicPersonelRequestDto) {
        try {
            AcademicPersonel academicPersonel = academicPersonelRepository.findByAcademicPersonelId(academicPersonelId);
            if (academicPersonel == null || academicPersonel.getIsDeleted()) throw new InvalidIdException("Geçersiz id");

            Department department = departmentRepository.findByDepartmentId(academicPersonelRequestDto.getDepartment_id());
            academicPersonel.setName(academicPersonelRequestDto.getName());
            academicPersonel.setE_mail(academicPersonelRequestDto.getE_mail());
            if (!isNullOrEmpty(academicPersonelRequestDto.getDepartment_id())) {
                academicPersonel.setDepartment(department);
            }
            departmentRepository.save(department);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public List<AcademicPersonelResponseDto> findByDepartmentId(String departmentId) {
        return academicPersonelRepository.findByDepartmentId(departmentId).stream()
                .map(academicPersonel -> modelMapper.map(academicPersonel,AcademicPersonelResponseDto.class))
                .collect(Collectors.toList());

    }

    public List<AcademicPersonelResponseDto> findByQueryOfName(String query) {
        Pageable pageable = PageRequest.of(0, 20);
        return academicPersonelRepository.findByQueryOfName(query,pageable).stream()
                .map(academicPersonel -> modelMapper.map(academicPersonel,AcademicPersonelResponseDto.class))
                .collect(Collectors.toList());
    }
}
