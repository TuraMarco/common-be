package it.turamarco.commonbe.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank
	@Size(max = 120)
	private String username;

	@NotBlank
	@Size(max = 120)
	private String password;
}
