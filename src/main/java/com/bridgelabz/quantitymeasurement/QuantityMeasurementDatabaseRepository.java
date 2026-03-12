package com.bridgelabz.quantitymeasurement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JDBC-based repository implementation that persists
 * {@link QuantityMeasurementEntity} instances into a relational database using
 * the H2 driver configured in {@link ApplicationConfig}.
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final String SCHEMA_RESOURCE = "/db/schema.sql";

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public QuantityMeasurementDatabaseRepository() {
        initializeSchema();
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            return;
        }
        String sql = "INSERT INTO quantity_measurement (" +
                "operation_type, measurement_type, " +
                "left_value, left_unit, " +
                "right_value, right_unit, " +
                "result_value, result_unit, " +
                "scalar_result, error_flag, error_message, created_at" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connectionPool.acquire();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            QuantityDTO.MeasurementType measurementType = null;
            if (entity.getLeftOperand() != null && entity.getLeftOperand().getUnit() != null) {
                measurementType = detectMeasurementType(entity.getLeftOperand().getUnit());
            }

            ps.setString(1, entity.getOperationType() != null ? entity.getOperationType().name() : null);
            ps.setString(2, measurementType != null ? measurementType.name() : null);

            if (entity.getLeftOperand() != null) {
                ps.setDouble(3, entity.getLeftOperand().getValue());
                ps.setString(4, entity.getLeftOperand().getUnit() != null
                        ? entity.getLeftOperand().getUnit().getUnitName()
                        : null);
            } else {
                ps.setObject(3, null);
                ps.setString(4, null);
            }

            if (entity.getRightOperand() != null) {
                ps.setDouble(5, entity.getRightOperand().getValue());
                ps.setString(6, entity.getRightOperand().getUnit() != null
                        ? entity.getRightOperand().getUnit().getUnitName()
                        : null);
            } else {
                ps.setObject(5, null);
                ps.setString(6, null);
            }

            if (entity.getResultQuantity() != null) {
                ps.setDouble(7, entity.getResultQuantity().getValue());
                ps.setString(8, entity.getResultQuantity().getUnit() != null
                        ? entity.getResultQuantity().getUnit().getUnitName()
                        : null);
            } else {
                ps.setObject(7, null);
                ps.setString(8, null);
            }

            if (entity.getScalarResult() != null) {
                ps.setDouble(9, entity.getScalarResult());
            } else {
                ps.setObject(9, null);
            }

            ps.setBoolean(10, entity.hasError());
            ps.setString(11, entity.getErrorMessage());
            ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save quantity measurement entity", e);
        } finally {
            // Connection is auto-closed by try-with-resources and returned to pool
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        String sql = "SELECT * FROM quantity_measurement";
        return queryEntities(sql, ps -> {
        });
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(
            QuantityMeasurementEntity.OperationType operationType) {
        String sql = "SELECT * FROM quantity_measurement WHERE operation_type = ?";
        return queryEntities(sql, ps -> ps.setString(1, operationType.name()));
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(
            QuantityDTO.MeasurementType measurementType) {
        String sql = "SELECT * FROM quantity_measurement WHERE measurement_type = ?";
        return queryEntities(sql, ps -> ps.setString(1, measurementType.name()));
    }

    @Override
    public long getTotalCount() {
        String sql = "SELECT COUNT(*) FROM quantity_measurement";
        try (Connection conn = connectionPool.acquire();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get total count of measurements", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM quantity_measurement";
        try (Connection conn = connectionPool.acquire();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete all measurements", e);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.shutdown();
    }

    private interface StatementConfigurer {
        void configure(PreparedStatement ps) throws SQLException;
    }

    private List<QuantityMeasurementEntity> queryEntities(String sql, StatementConfigurer configurer) {
        List<QuantityMeasurementEntity> result = new ArrayList<>();
        try (Connection conn = connectionPool.acquire();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            configurer.configure(ps);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query quantity measurements", e);
        }
        return result;
    }

    private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {
        QuantityMeasurementEntity.OperationType operationType = null;
        String op = rs.getString("operation_type");
        if (op != null) {
            operationType = QuantityMeasurementEntity.OperationType.valueOf(op);
        }

        String measurementTypeStr = rs.getString("measurement_type");
        QuantityDTO.MeasurementType measurementType = null;
        if (measurementTypeStr != null) {
            measurementType = QuantityDTO.MeasurementType.valueOf(measurementTypeStr);
        }

        QuantityModel<?> left = null;
        Double leftValue = (Double) rs.getObject("left_value");
        String leftUnit = rs.getString("left_unit");
        if (leftValue != null && leftUnit != null && measurementType != null) {
            left = new QuantityModel<>(leftValue, toUnit(measurementType, leftUnit));
        }

        QuantityModel<?> right = null;
        Double rightValue = (Double) rs.getObject("right_value");
        String rightUnit = rs.getString("right_unit");
        if (rightValue != null && rightUnit != null && measurementType != null) {
            right = new QuantityModel<>(rightValue, toUnit(measurementType, rightUnit));
        }

        QuantityModel<?> resultQuantity = null;
        Double resultValue = (Double) rs.getObject("result_value");
        String resultUnit = rs.getString("result_unit");
        if (resultValue != null && resultUnit != null && measurementType != null) {
            resultQuantity = new QuantityModel<>(resultValue, toUnit(measurementType, resultUnit));
        }

        Double scalarResult = (Double) rs.getObject("scalar_result");
        boolean errorFlag = rs.getBoolean("error_flag");
        String errorMessage = rs.getString("error_message");

        if (errorFlag) {
            return new QuantityMeasurementEntity(left, right, operationType, errorMessage);
        }

        if (scalarResult != null) {
            return new QuantityMeasurementEntity(left, right, operationType, scalarResult);
        }

        if (right != null) {
            return new QuantityMeasurementEntity(left, right, operationType, resultQuantity);
        }

        return new QuantityMeasurementEntity(left, operationType, resultQuantity);
    }

    private IMeasurable toUnit(QuantityDTO.MeasurementType type, String unitName) {
        switch (type) {
            case LENGTH:
                return LengthUnit.valueOf(unitName);
            case WEIGHT:
                return WeightUnit.valueOf(unitName);
            case VOLUME:
                return VolumeUnit.valueOf(unitName);
            case TEMPERATURE:
                return TemperatureUnit.valueOf(unitName);
            default:
                throw new DatabaseException("Unsupported measurement type: " + type);
        }
    }

    private QuantityDTO.MeasurementType detectMeasurementType(IMeasurable unit) {
        if (unit instanceof LengthUnit) {
            return QuantityDTO.MeasurementType.LENGTH;
        } else if (unit instanceof WeightUnit) {
            return QuantityDTO.MeasurementType.WEIGHT;
        } else if (unit instanceof VolumeUnit) {
            return QuantityDTO.MeasurementType.VOLUME;
        } else if (unit instanceof TemperatureUnit) {
            return QuantityDTO.MeasurementType.TEMPERATURE;
        }
        return null;
    }

    private void initializeSchema() {
        try (Connection conn = connectionPool.acquire()) {
            String ddl = loadSchemaSql();
            if (ddl != null && !ddl.trim().isEmpty()) {
                for (String statement : ddl.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        try (PreparedStatement ps = conn.prepareStatement(trimmed)) {
                            ps.execute();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        }
    }

    private String loadSchemaSql() {
        try (InputStream in = getClass().getResourceAsStream(SCHEMA_RESOURCE)) {
            if (in == null) {
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to load schema.sql", e);
        }
    }
}

