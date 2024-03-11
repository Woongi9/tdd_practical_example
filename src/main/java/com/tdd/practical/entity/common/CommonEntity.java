package com.tdd.practical.entity.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class CommonEntity {
	@Id
	@GeneratedValue
	long id;

	public void setId(long id) {
		this.id = id;
	}
}
