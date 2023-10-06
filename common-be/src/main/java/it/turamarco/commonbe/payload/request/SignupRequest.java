package it.turamarco.commonbe.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	@Size(max = 120)
	private String username;

	@NotBlank
	@Size(max = 120)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6,max = 120)
	private String password;
	
	private Set<String> roles;
}
