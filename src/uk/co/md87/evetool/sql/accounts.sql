CREATE TABLE Accounts (
    account_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_userid INT,
    account_key VARCHAR(100)
)