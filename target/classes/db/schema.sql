CREATE TABLE IF NOT EXISTS quantity_measurement (
    id IDENTITY PRIMARY KEY,
    operation_type VARCHAR(32) NOT NULL,
    measurement_type VARCHAR(32),

    left_value DOUBLE,
    left_unit VARCHAR(64),

    right_value DOUBLE,
    right_unit VARCHAR(64),

    result_value DOUBLE,
    result_unit VARCHAR(64),

    scalar_result DOUBLE,

    error_flag BOOLEAN NOT NULL DEFAULT FALSE,
    error_message VARCHAR(512),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_quantity_measurement_operation
    ON quantity_measurement (operation_type);

CREATE INDEX IF NOT EXISTS idx_quantity_measurement_type
    ON quantity_measurement (measurement_type);

