package com.spdu.dal.mappers;

import com.spdu.domain_models.entities.FileEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileEntityMapper implements RowMapper<FileEntity> {

    @Override
    public FileEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(rs.getString("name"));
        fileEntity.setPath(rs.getString("path"));
        fileEntity.setContentType(rs.getString("content_type"));
        fileEntity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        fileEntity.setOwnerId(rs.getLong("owner_id"));
        return fileEntity;
    }
}
