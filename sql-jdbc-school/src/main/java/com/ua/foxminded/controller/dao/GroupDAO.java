package com.ua.foxminded.controller.dao;

import java.util.List;
import java.util.Map;

import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Group;

public interface GroupDAO {
    void createGroup(Group group) throws SchoolDAOException;
    void createGroups(List<Group> groups) throws SchoolDAOException;
    Map<Group, Integer> getGroupsByStudentCount(int count) throws SchoolDAOException;
    List<Group> getGroups() throws SchoolDAOException;
}
