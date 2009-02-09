CREATE TABLE PageConfigElements (
    pce_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pce_type INT,
    pce_value VARCHAR(512),
    pce_next INT
)