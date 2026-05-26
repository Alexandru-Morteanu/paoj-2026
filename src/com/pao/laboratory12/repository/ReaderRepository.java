package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderRepository implements Repository<Reader, Long> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Reader mapRow(ResultSet rs) throws SQLException {
        Reader reader = new Reader();

        reader.setId(rs.getLong("id"));
        reader.setName(rs.getString("name"));
        reader.setEmail(rs.getString("email"));

        return reader;
    }

    @Override
    public void save(Reader reader) throws SQLException {
        String sql = "INSERT INTO reader (name, email) VALUES (?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getEmail());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    reader.setId(keys.getLong(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Reader> findById(Long id) throws SQLException {
        String sql = "SELECT id, name, email FROM reader WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Reader> findAll() throws SQLException {
        String sql = "SELECT id, name, email FROM reader ORDER BY id";
        List<Reader> readers = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                readers.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return readers;
    }

    @Override
    public void update(Reader reader) throws SQLException {
        String sql = "UPDATE reader SET name = ?, email = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getEmail());
            ps.setLong(3, reader.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM reader WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}