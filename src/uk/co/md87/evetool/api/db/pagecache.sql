CREATE TABLE PageCache (
    pc_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    pc_method VARCHAR(512),
    pc_args VARCHAR(512),
    pc_cachedat TIMESTAMP,
    pc_cacheduntil TIMESTAMP,
    pc_data CLOB
)