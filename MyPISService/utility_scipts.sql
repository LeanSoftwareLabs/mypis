LOAD DATA INFILE 'C:/JDeveloper/mywork/MyPIS/MyPISService/ChartOfAccounts.csv'
INTO TABLE gl_accounts
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(ACCT_CODE,ACCT_NAME,ACCT_DESC, ACCT_TYPE,  Expense_Claims, payments)
set gl_acct_id = null, tenant_id = 1, dashboard = 0;