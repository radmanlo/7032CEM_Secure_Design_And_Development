package com.example.hab.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User{

    @Id
    private String id;
    private Role role = Role.USER;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;


}









//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Document(collection = "User")
//public class User implements UserDetails {
//
//    @Id
//    private String id;
//
//    private Role role = Role.USER;
//    private String firstName;
//    private String lastName;
//
//    private String password;
//    private String email;
//
//    @JsonFormat(pattern = "MM-dd-YYYY")
//    private Date birthday;
//
//    private String allergies;
//    private String diseases;
//    private boolean consent;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
