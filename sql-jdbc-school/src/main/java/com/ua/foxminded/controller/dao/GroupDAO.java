package com.ua.foxminded.controller.dao;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Group;

public interface GroupDAO {
    void create(Group group) throws SchoolDAOException;
    void create(List<Group> groups) throws SchoolDAOException;
    Map<Group, Integer> getByStudentCount(int count) throws SchoolDAOException;
    List<Group> showAll() throws SchoolDAOException;
}
