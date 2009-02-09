CREATE TABLE PageConfigs (
    pc_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pc_page VARCHAR(50),
    pc_tl INT,
    pc_tr INT,
    pc_bl INT,
    pb_br INT
)