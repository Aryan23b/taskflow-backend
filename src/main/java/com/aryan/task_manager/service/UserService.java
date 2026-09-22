package com.aryan.task_manager.service;


import com.aryan.task_manager.dto.UserRequest;
import com.aryan.task_manager.dto.UserResponse;
import com.aryan.task_manager.entity.User;
import com.aryan.task_manager.repository.ProjectRepository;
import com.aryan.task_manager.repository.TaskRepository;
import com.aryan.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserRequest request){
        String email=request.getEmail().trim()
                .toLowerCase(Locale.ROOT);


        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("user email exists: "+email);
        }

        User user=User.builder()
                .name(request.getName().trim())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser=userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly =true)
    public List<UserResponse> getAllUsers(){

        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly=true)
    public UserResponse getUserById(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found :"+id
                        ));

        return  toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        if (projectRepository.existsByOwnerId(id)) {
            throw new IllegalStateException(
                    "Cannot delete a user who owns projects."
            );
        }

        taskRepository.clearAssignedUser(id);
        userRepository.delete(user);
    }




    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
