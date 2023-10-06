package it.turamarco.commonbe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "roles")
public class Role {
	@Id
	private String id;

	private ERole name;

	public Role(ERole name) {
		this.name = name;
	}
}
