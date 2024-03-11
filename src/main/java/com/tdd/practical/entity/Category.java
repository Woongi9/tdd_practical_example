package com.tdd.practical.entity;

import java.util.List;

import com.tdd.practical.entity.common.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category extends CommonEntity {

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
