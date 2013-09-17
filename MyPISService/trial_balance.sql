select acct_code, IF(acct_code IS NULL, 'TOTAL', acct_name) as acct_name, sum(BeginningBalance) as BeginningBalance,
        sum(DEBIT) as DEBIT, sum(CREDIT) as CREDIT, sum(BeginningBalance)+ sum(DEBIT) - sum(CREDIT) as EndingBalance
        from(select acct_code, acct_name, acct_type, sum(debit-credit) as BeginningBalance, 0 as DEBIT, 0 as CREDIT
        from gl_trans_detail d left join gl_accounts a on (d.gl_account_id=a.gl_acct_id)
        inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
        where reporting_date < '2013-9-13' 
        group by acct_code, acct_name, acct_type
        union all
        select acct_code, acct_name, acct_type, 0 as BeginningBalance,
        sum(debit) as DEBIT,
        sum(credit) as CREDIT
        from gl_trans_detail d left join gl_accounts a on (d.gl_account_id=a.gl_acct_id)
        inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
        where reporting_date between '2012-9-13' and '2013-9-13'
        group by acct_code, acct_name, acct_type) tmp
        GROUP BY acct_code WITH ROLLUP