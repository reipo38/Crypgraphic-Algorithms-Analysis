package com.dreamteam.algorithm.analysis.web.service.user;

import com.dreamteam.algorithm.analysis.config.exception.IncorrectPasswordException;
import com.dreamteam.algorithm.analysis.model.requests.change.EmailChangeRequest;
import com.dreamteam.algorithm.analysis.model.requests.change.PasswordChangeRequest;
import com.dreamteam.algorithm.analysis.model.User;
import com.dreamteam.algorithm.analysis.model.requests.change.ChangeRequest;
import com.dreamteam.algorithm.analysis.model.requests.change.UsernameChangeRequest;
import com.dreamteam.algorithm.analysis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }

    public void changePassword(PasswordChangeRequest request, Principal principal) {
        var user = loadUserByUsername(principal.getName());
        throwIfPasswordIncorrect(request, user);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void changeEmail(EmailChangeRequest request, User user) {
        throwIfPasswordIncorrect(request, user);
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }

    public void changeUsername(UsernameChangeRequest request, User user) {
        throwIfPasswordIncorrect(request, user);
        user.setUsername(request.getUsername());
        userRepository.save(user);
    }

    private void throwIfPasswordIncorrect(ChangeRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
    }

}
