package com.ua.foxminded.dao;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Group;

public interface GroupDAO {
    void create(Group group) throws DAOException;
    void create(List<Group> groups) throws DAOException;
    Map<Group, Integer> getMinStudentCount() throws DAOException;
    Map<Group, Integer> getByStudentCount(int count) throws DAOException;
    List<Group> showAll() throws DAOException;
}
