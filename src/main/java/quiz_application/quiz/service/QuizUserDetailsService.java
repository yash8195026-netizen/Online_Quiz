package quiz_application.quiz.service;

import java.util.*;
import org.springframework.stereotype.Service;
import quiz_application.quiz.model.User;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import quiz_application.quiz.repository.UserRepository;

@Service
public class QuizUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


   public QuizUserDetailsService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
    }
    public void registerUser(String username,String password, String email, String role){
      User user = new User(username,passwordEncoder.encode(password),email,role);
      userRepository.save(user);
    }

    public boolean usernameExists(String username){
      return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email){
      return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email){
      return userRepository.findByEmail(email)
      .orElseThrow(() ->new RuntimeException("User not found"));
    }

    public void updatePassword(
        String email,
        String newPassword){

      User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void registerUser(User user){
      if(userRepository.existsByUsername(user.getUsername())){
        throw new RuntimeException("Username Already Exists");
      }
      if(userRepository.existsByEmail(user.getEmail())){
        throw new RuntimeException("Email Already Exists");
      }
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
    }

    public void printUsers() {
        userRepository.findAll().forEach(System.out::println);
    }

    public User findByUsername(String username){
      return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    } 

    public void updateProfile(String currentUsername,String username,String email){
      User user = findByUsername(currentUsername);
      user.setUsername(username);
      user.setEmail(email);
      userRepository.save(user);
    } 

    public void updateProfilePicture(String username,String fileName){

      User user = findByUsername(username);
      user.setProfilePicture(fileName);
      userRepository.save(user);
    }

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail)throws UsernameNotFoundException {

    User user = userRepository.findByUsername(usernameOrEmail)
    .orElseGet(() ->userRepository.findByEmail(usernameOrEmail)
    .orElseThrow(() ->new UsernameNotFoundException("User Not Found")));  
    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
  }
}