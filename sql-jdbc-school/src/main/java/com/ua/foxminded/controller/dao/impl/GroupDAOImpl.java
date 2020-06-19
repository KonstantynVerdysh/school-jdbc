package com.ua.foxminded.controller.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ua.foxminded.controller.dao.ConnectionFactory;
import com.ua.foxminded.controller.dao.GroupDAO;
import com.ua.foxminded.controller.dao.exceptions.SchoolDAOException;
import com.ua.foxminded.model.Group;

public class GroupDAOImpl implements GroupDAO {
    
    @Override
    public void create(Group group) throws SchoolDAOException {
        String sql = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, group.getName());
            pStatement.execute();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't write group.");
        } 
    }

    @Override
    public void create(List<Group> groups) throws SchoolDAOException {
        String sql = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Group group : groups) {
                pStatement.setString(1, group.getName());
                pStatement.addBatch();
            }
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't write groups.");
        } 
    }

    @Override
    public Map<Group, Integer> getByStudentCount(int maxCount) throws SchoolDAOException {
        String sql = "SELECT g.group_id, g.group_name,COUNT(s.group_id) AS students FROM groups g JOIN" + 
                " students s USING (group_id) GROUP BY g.group_id, g.group_name HAVING COUNT(*) <= ?;";
        Map<Group, Integer> result = new HashMap<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, maxCount);
            try (ResultSet resultSet = pStatement.executeQuery()) {
                while (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt(1));
                    group.setName(resultSet.getString(2));
                    int count = resultSet.getInt(3);
                    result.put(group, count);
                }
            }
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't get student by count.");
        }
        return result;
    }

    @Override
    public List<Group> showAll() throws SchoolDAOException {
        String sql = "SELECT group_id, group_name FROM groups;";
        List<Group> result = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql);
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt(1));
                group.setName(resultSet.getString(2));
                result.add(group);
            }
        } catch (SQLException e) {
            throw new SchoolDAOException("Can't read groups");
        }
        return result;
    }
}
