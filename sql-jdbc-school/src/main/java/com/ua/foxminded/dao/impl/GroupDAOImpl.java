package com.ua.foxminded.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ua.foxminded.dao.ConnectionFactory;
import com.ua.foxminded.dao.GroupDAO;
import com.ua.foxminded.dao.exceptions.DAOException;
import com.ua.foxminded.model.Group;

public class GroupDAOImpl implements GroupDAO {
    private ConnectionFactory connectionFactory;
    
    public GroupDAOImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void create(Group group) throws DAOException {
        String sql = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, group.getName());
            pStatement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't write group.");
        } 
    }

    @Override
    public void create(List<Group> groups) throws DAOException {
        String sql = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql)) {
            for (Group group : groups) {
                pStatement.setString(1, group.getName());
                pStatement.addBatch();
            }
            pStatement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Can't write groups.");
        } 
    }

    @Override
    public Map<Group, Integer> getMinStudentCount() throws DAOException {
        String sql = "SELECT g.group_id, g.group_name, COUNT(s.group_id) FROM groups g JOIN students s USING (group_id)" +
                 " GROUP BY g.group_id, g.group_name HAVING COUNT(*) = (SELECT MIN(COUNT) FROM (SELECT g.group_name, COUNT(s.student_id)" +
                 " FROM groups g JOIN students s USING (group_id) GROUP BY g.group_name) g);";
        Map<Group, Integer> result = new HashMap<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql);
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt(1));
                group.setName(resultSet.getString(2));
                int count = resultSet.getInt(3);
                result.put(group, count);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't get group with min student count.");
        }
        return result;
    }

    @Override
    public Map<Group, Integer> getByStudentCount(int maxCount) throws DAOException {
        String sql = "SELECT g.group_id, g.group_name,COUNT(s.group_id) AS students FROM groups g JOIN" + 
                " students s USING (group_id) GROUP BY g.group_id, g.group_name HAVING COUNT(*) <= ?;";
        Map<Group, Integer> result = new HashMap<>();
        try (Connection connection = connectionFactory.getConnection();
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
            throw new DAOException("Can't get student by count.");
        }
        return result;
    }

    @Override
    public List<Group> showAll() throws DAOException {
        String sql = "SELECT group_id, group_name FROM groups;";
        List<Group> result = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(sql);
             ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt(1));
                group.setName(resultSet.getString(2));
                result.add(group);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't read groups");
        }
        return result;
    }
}
