package com.edu.harran.social.service;

import com.edu.harran.social.dto.academicDto.AcademicRequestDto;
import com.edu.harran.social.dto.academicDto.AcademicResponseDto;
import com.edu.harran.social.entity.Academic;
import com.edu.harran.social.exception.academic.AcademicCreateException;
import com.edu.harran.social.exception.crudException.InvalidIdException;
import com.edu.harran.social.repository.AcademicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicService {

    private final AcademicRepository academicRepository;
    private final ModelMapper modelMapper;
    public void insert(AcademicRequestDto academicRequest){
        try {
            Academic findAcademicByName = academicRepository.findByName(academicRequest.getName());
            if (findAcademicByName!=null) throw new AcademicCreateException(academicRequest.getName()+" zaten mevcut");

            Academic academic = new Academic();
            academic.setName(academicRequest.getName());
            academicRepository.save(academic);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(String academicId){
        try {
            Academic academic = academicRepository.findByAcademicId(academicId);
            if(academic==null || academic.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            academic.setIsDeleted(true);
            academicRepository.save(academic);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String academicId,AcademicRequestDto academicRequestDto){
        try {
            Academic academic = academicRepository.findByAcademicId(academicId);
            if(academic==null || academic.getIsDeleted()) throw new InvalidIdException("Geçersiz id");
            if(!isNullOrEmpty(academicRequestDto.getName())){
                academic.setName(academicRequestDto.getName());
                academicRepository.save(academic);
            }
        } catch (Exception e) {
        throw new RuntimeException(e);
        }
    }

    public List<AcademicResponseDto> findAll() {
        List<Academic> academics = academicRepository.findAll();
        List<AcademicResponseDto> academicsList = new ArrayList<>();
        for (Academic academic : academics) {
            academicsList.add(modelMapper.map(academic, AcademicResponseDto.class));
        }
        return academicsList;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
