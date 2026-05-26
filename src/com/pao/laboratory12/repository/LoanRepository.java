package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Loan;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository implements Repository<Loan, Long> {
    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan loan = new Loan();

        loan.setId(rs.getLong("id"));
        loan.setBookId(rs.getLong("book_id"));
        loan.setReaderId(rs.getLong("reader_id"));
        loan.setLoanDate(rs.getString("loan_date"));
        loan.setReturnDate(rs.getString("return_date"));

        return loan;
    }

    @Override
    public void save(Loan loan) throws SQLException {
        String sql = "INSERT INTO loan (book_id, reader_id, loan_date, return_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, loan.getBookId());
            ps.setLong(2, loan.getReaderId());
            ps.setString(3, loan.getLoanDate());
            ps.setString(4, loan.getReturnDate());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    loan.setId(keys.getLong(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Loan> findById(Long id) throws SQLException {
        String sql = "SELECT id, book_id, reader_id, loan_date, return_date FROM loan WHERE id = ?";

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
    public List<Loan> findAll() throws SQLException {
        String sql = "SELECT id, book_id, reader_id, loan_date, return_date FROM loan ORDER BY id";
        List<Loan> loans = new ArrayList<>();

        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return loans;
    }

    @Override
    public void update(Loan loan) throws SQLException {
        String sql = "UPDATE loan SET book_id = ?, reader_id = ?, loan_date = ?, return_date = ? WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, loan.getBookId());
            ps.setLong(2, loan.getReaderId());
            ps.setString(3, loan.getLoanDate());
            ps.setString(4, loan.getReturnDate());
            ps.setLong(5, loan.getId());

            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM loan WHERE id = ?";

        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}