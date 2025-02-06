package com.edu.harran.social.service;

import com.edu.harran.social.entity.VerificationCode;
import com.edu.harran.social.repository.VerificaitonCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodesService {
    private final VerificaitonCodesRepository verificaitonCodesRepository;

    public void create(int vCode,String email) {
       VerificationCode verificationCode = VerificationCode.builder().code(vCode).email(email).build();
       verificaitonCodesRepository.save(verificationCode);
    }

    public VerificationCode checkCode(String email) {
        return verificaitonCodesRepository.findByCode(email);
    }
}
