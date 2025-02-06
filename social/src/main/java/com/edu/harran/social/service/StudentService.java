package com.edu.harran.social.service;

import com.edu.harran.social.dto.studentDto.StudentRequestDto;
import com.edu.harran.social.dto.studentDto.StudentResponseDto;
import com.edu.harran.social.entity.*;
import com.edu.harran.social.exception.crudException.InvalidIdException;
import com.edu.harran.social.repository.DepartmentRepository;
import com.edu.harran.social.repository.StudentRepository;
import com.edu.harran.social.repository.UserRepository;
import com.edu.harran.social.websocket.entity.Chat;
import com.edu.harran.social.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final ChatService chatService;
    private final UserApprovalPendingService userApprovalPendingService;
    private final UserService userService;

    public void create(StudentRequestDto studentRequestDto,String token) {
        try {
            UserApprovalPending userApprovalPending = userApprovalPendingService.findByUserApprovalPendingId(studentRequestDto.getUserApprovalPendingId());
            Department department = departmentRepository.findByDepartmentId(studentRequestDto.getDepartmentId());
            if(department==null||userApprovalPending==null) throw new InvalidIdException("geçersiz id");

            User createNewUser = new User();
            createNewUser.setEmail(userApprovalPending.getEmail());
            createNewUser.setName(studentRequestDto.getName());
            createNewUser.setPassword(userApprovalPending.getPassword());
            createNewUser.setSurname(studentRequestDto.getSurname());
            createNewUser.setUserType(UserType.STUDENT);

            User createdUser = userRepository.save(createNewUser);
            if(createdUser.getUserId()!=null) {
                Student student = new Student();
                student.setUser(createdUser);
                student.setDepartment(department);
                studentRepository.save(student);
                userApprovalPending.setStatus(UserApprovalPendingStatus.APPROVED);
                this.addUserToDepartmentGroupHandle(student,token);
                userApprovalPendingService.save(userApprovalPending);
            }else userRepository.delete(createdUser);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void addUserToDepartmentGroupHandle(Student student,String token){
        String email = jwtService.extractEmail(token.substring(7));
        User userAdmin = userRepository.findByEmail(email);
        List<Chat> chatsByDepartment = chatService.findChatByDepartmentId(student.getDepartment().getId());
        for (Chat chat : chatsByDepartment){
            chatService.addUserToDepartmentGroup(chat.getChatId(), student.getUser().getUserId(), userAdmin.getUserId());
        }
    }

    public List<StudentResponseDto> findAll() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDto> studentResponseDtoList = new ArrayList<>();
        for (Student student : students) {
            studentResponseDtoList.add(modelMapper.map(student, StudentResponseDto.class));
        }
        return studentResponseDtoList;
    }

    public void delete(String studentId) {
        try{
            Student findStudent = studentRepository.findByStudentId(studentId);
            if (findStudent==null || findStudent.getIsDeleted()) throw new InvalidIdException("geçersiz id");
            User findUser = findStudent.getUser();
            findUser.setIsDeleted(true);
            findStudent.setIsDeleted(true);
            studentRepository.save(findStudent);
            userRepository.save(findUser);
        } catch (Exception e){
           throw new RuntimeException(e);
        }
    }

    public void update(String studentId,StudentRequestDto studentRequestDto) {
        try{
            Department department = null;
            Student findStudent = studentRepository.findByStudentId(studentId);
            if (findStudent==null || findStudent.getIsDeleted()) throw new InvalidIdException("geçersiz id");
            /*if(studentRequestDto.getDepartment_id()!=null){
                department = departmentRepository.findByDepartmentId(studentRequestDto.getDepartment_id());
            }*/
            User findUser = findStudent.getUser();
            findStudent.setDepartment(department==null?findStudent.getDepartment():department);
            findUser.setName(studentRequestDto.getName()==null?findUser.getName():studentRequestDto.getName());
            findUser.setSurname(studentRequestDto.getSurname()==null?findUser.getSurname():studentRequestDto.getSurname());
            //findUser.setEmail(studentRequestDto.getE_mail()==null?findUser.getEmail():studentRequestDto.getE_mail());
            findUser.setStudent(findStudent);
            //if(studentRequestDto.getPassword()!=null)findUser.setPassword(passwordEncoder.encode(studentRequestDto.getPassword()));
            studentRepository.save(findStudent);
            userRepository.saveAndFlush(findUser);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public StudentResponseDto findOneStudent(String token) {
        String email = jwtService.extractEmail(token.substring(7));
        User user = userService.findUserByEmail(email);
        if(user!=null){
            return modelMapper.map(user.getStudent(),StudentResponseDto.class);
        }
        return null;
    }
}
