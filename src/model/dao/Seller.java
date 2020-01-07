package model.dao;

import model.entities.Department;

import java.util.List;

public interface Seller {

	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
}
