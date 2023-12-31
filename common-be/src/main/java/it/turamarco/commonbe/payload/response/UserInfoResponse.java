package it.turamarco.commonbe.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
	private String id;
	private String username;
	private String email;
	private List<String> roles;

}
