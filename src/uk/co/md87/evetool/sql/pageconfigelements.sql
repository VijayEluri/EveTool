CREATE TABLE PageConfigElements (
    pce_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pc_id INT,
    pce_location INT,
    pce_order INT,
    pce_type INT,
    pce_value VARCHAR(512)
)