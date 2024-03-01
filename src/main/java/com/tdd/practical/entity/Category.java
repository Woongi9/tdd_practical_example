package com.tdd.practical.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

	@Id
	@GeneratedValue
	long id;

	@Column
	String name;

	@OneToMany(fetch = FetchType.LAZY)
	List<MapCategoryAndPost> postMapList;

	@Builder
	public Category(String name, List<MapCategoryAndPost> postMapList) {
		this.name = name;
		this.postMapList = postMapList;
	}
}
