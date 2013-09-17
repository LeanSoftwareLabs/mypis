select type_sub_group, acct_type, acct_code, sum(amount), sum(comparative1), sum(comparative2) from(
select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, 
		coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount, 0 as comparative1, 0 as comparative2
from gl_trans_detail d 
		inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
		inner join gl_account_types t on (a.acct_type = t.type_name)
		inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
		where h.tenant_id = 1 and type_group = '4-Nominal'
		and reporting_date between '2013-1-1' and '2013-9-13'
group by type_sub_group, acct_type, acct_code, index_
union all select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, 0 as amount,
		coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2
from gl_trans_detail d 
		inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
		inner join gl_account_types t on (a.acct_type = t.type_name)
		inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
		where 1 > 0
		and h.tenant_id = 1
		and type_group = '4-Nominal'
		and reporting_date between date_sub('2013-1-1', INTERVAL 1 YEAR) and date_sub('2013-9-13', INTERVAL 1 YEAR)
group by index_, type_sub_group, acct_type, acct_code
union all
select index_, type_sub_group, acct_type, concat(acct_code,': ',acct_name) as acct_code, 0 as amount,
		coalesce(sum(debit),0)- coalesce(sum(credit),0) as comparative1, 0 as comparative2
from gl_trans_detail d 
		inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
		inner join gl_account_types t on (a.acct_type = t.type_name)
		inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
		where 1 > 1
		and h.tenant_id = 1
		and type_group = '4-Nominal'
		and reporting_date between date_sub('2013-1-1', INTERVAL 2 YEAR) and date_sub('2013-9-13', INTERVAL 2 YEAR)
group by index_, type_sub_group, acct_type, acct_code
)v group by type_sub_group, acct_type, acct_code
order by index_, acct_code;