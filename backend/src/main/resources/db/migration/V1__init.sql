CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE clients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    notes TEXT,
    user_id UUID REFERENCES users (id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE materials (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    price_per_kg NUMERIC(12, 2) NOT NULL,
    current_stock_grams NUMERIC(12, 2) NOT NULL,
    density_grams_per_cm3 NUMERIC(6, 3),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE settings (
    id UUID PRIMARY KEY,
    currency VARCHAR(3) NOT NULL,
    electricity_rate_per_kwh NUMERIC(12, 4) NOT NULL,
    printer_power_consumption_watts NUMERIC(10, 2) NOT NULL,
    printer_purchase_price NUMERIC(12, 2) NOT NULL,
    printer_lifespan_hours NUMERIC(10, 2) NOT NULL,
    default_margin_percentage NUMERIC(5, 2) NOT NULL,
    labor_hourly_rate NUMERIC(12, 2) NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE electricity_consumption_records (
    id UUID PRIMARY KEY,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    kwh_consumed NUMERIC(12, 3) NOT NULL,
    total_bill_amount NUMERIC(12, 2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE quotes (
    id UUID PRIMARY KEY,
    client_id UUID REFERENCES clients (id),
    material_id UUID NOT NULL REFERENCES materials (id),
    title VARCHAR(255) NOT NULL,
    material_grams_used NUMERIC(10, 2) NOT NULL,
    print_time_hours NUMERIC(10, 2) NOT NULL,
    labor_hours NUMERIC(10, 2) NOT NULL,
    packaging_cost NUMERIC(12, 2) NOT NULL,
    material_cost NUMERIC(12, 2) NOT NULL,
    electricity_cost NUMERIC(12, 2) NOT NULL,
    printer_wear_cost NUMERIC(12, 2) NOT NULL,
    labor_cost NUMERIC(12, 2) NOT NULL,
    margin_percentage NUMERIC(5, 2) NOT NULL,
    total_cost NUMERIC(12, 2) NOT NULL,
    suggested_price NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users (id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id UUID,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_clients_name ON clients (name);
CREATE INDEX idx_materials_name ON materials (name);
CREATE INDEX idx_quotes_client_id ON quotes (client_id);
CREATE INDEX idx_quotes_status ON quotes (status);
CREATE INDEX idx_audit_logs_user_id ON audit_logs (user_id);
