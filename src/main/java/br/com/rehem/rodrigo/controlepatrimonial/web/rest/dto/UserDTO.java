package br.com.rehem.rodrigo.controlepatrimonial.web.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Authority;
import br.com.rehem.rodrigo.controlepatrimonial.domain.User;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;
    
    
    @Size(max = 10)
    private String cadastro;
    
    @Size(max = 50)
    private String carreira;
    
    @Size(max = 50)
    private String lotacao;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()),user.getCadastro(),user.getCarreira(),user.getLotacao());
    }

    public UserDTO(String login, String firstName, String lastName,
        String email, boolean activated, String langKey, Set<String> authorities, String cadastro, String carreira, String lotacao) {

        this.login = login;        
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.cadastro = cadastro;
        this.carreira = carreira; 
        this.lotacao = lotacao;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getCadastro() {
		return cadastro;
	}

	public String getCarreira() {
		return carreira;
	}

	public String getLotacao() {
		return lotacao;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", cadastro='" + cadastro + '\'' +
            ", carreira='" + carreira + '\'' +
            ", lotacao='" + lotacao + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}
