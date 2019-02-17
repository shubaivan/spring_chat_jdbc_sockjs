package com.spdu.dal.repositories;

import com.spdu.dal.mappers.FileEntityMapper;
import com.spdu.domain_models.entities.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class FileEntityRepositoryImpl implements FileEntityRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FileEntityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long create(FileEntity fileEntity) throws SQLException {
        String query = "INSERT INTO file_entities (" +
                "name, path," +
                " content_type, created_at," +
                " owner_id) VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, fileEntity.getName());
            ps.setString(2, fileEntity.getPath());
            ps.setString(3, fileEntity.getContentType());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(5, fileEntity.getOwnerId());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }

    @Override
    public Optional<FileEntity> getById(long id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM file_entities WHERE file_entities.id =?";

        FileEntity fileEntity = jdbcTemplate.queryForObject(query,
                new Object[]{id},
                new FileEntityMapper());
        return Optional.of(fileEntity);
    }

    @Override
    public FileEntity getByUserId(long id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM file_entities AS f\n" +
                "WHERE f.owner_id ="+id+" \n" +
                "ORDER BY f.id DESC \n" +
                "LIMIT 1";

        List<FileEntity> result = getByQuery(query);
        if (result.size() > 0) {
           return result.get(0);
        }

        return null;
    }

    private List<FileEntity> getByQuery(String query) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(query,
                rs -> {
                    List<FileEntity> list = new LinkedList<>();
                    while (rs.next()) {
                        list.add(new FileEntityMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
    }
}
