select case type_group when 'Asset' then '1-Asset' when 'Liabilities' then '2-Liabilities' when 'Capital' then '3-Capital' else 'xx' end as type_group,
type_sub_group, x.acct_type, x.acct_code,
case type_group when 'Asset' then amount when 'Liabilities' then -amount when 'Capital' then -amount else 0 end as amount
from(
	select * from (
		select coalesce(type_group,'xx') as type_group, coalesce(type_sub_group,'xx') as type_sub_group, coalesce(acct_type,'xx') as acct_type, coalesce(acct_code,'xx') as acct_code, amount from (
			select * from (
				select * from (
					select case type_group when 'Nominal' then 'Capital' else type_group end as type_group,
						case type_group when 'Nominal' then 'Equity' else type_sub_group end as type_sub_group, 
						case type_group when 'Nominal' then 'Retained Eaarnings' else acct_type end as acct_type, 
						case type_group when 'Nominal' then 'Unprocessed' else concat(acct_code,': ',acct_name) end as acct_code, 
						coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount
						from gl_trans_detail d 
						inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
						inner join gl_account_types t on (a.acct_type = t.type_name)
						inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
						where h.tenant_id = 1
						and reporting_date <= '2013-9-13'
						group by type_group, type_sub_group, acct_type, acct_code)t
				group by type_group, type_sub_group, acct_type, acct_code)u
			group by type_group, type_sub_group, acct_type, acct_code with rollup)v)w
		union all
			select distinct type_group, '' as type_sub_group, '' as acct_type, '' as acct_code, null as amount from gl_account_types where type_group <> 'Nominal'	
		union all
			select distinct type_group, type_sub_group, '' as acct_type, '' as acct_code, null as amount from gl_account_types where type_group <> 'Nominal') x
order by type_group, type_sub_group, acct_type, acct_code

;

select distinct type_group, type_sub_group from gl_account_types where type_group <> 'Nominal' order by index_;

select DATE_SUB('2013-9-14', INTERVAL 2 YEAR);

select type_group, type_sub_group, acct_type, acct_code, 
		if(type_group='1-Asset',sum(amount),-sum(amount)) as amount, 
		if(type_group='1-Asset',sum(comparative1),-sum(comparative1)) as comparative1,
		if(type_group='1-Asset',sum(comparative1),-sum(comparative2)) as comparative2 from (
	select case type_group when '4-Nominal' then '3-Capital' else type_group end as type_group,
		case type_group when '4-Nominal' then 'Equity' else type_sub_group end as type_sub_group, 
		case type_group when '4-Nominal' then 'Retained Eaarnings' else acct_type end as acct_type,
		case type_group when '4-Nominal' then 'Unprocessed' else concat(acct_code,': ',acct_name) end as acct_code, 
		coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount,
		if(0>0,coalesce(sum(if(reporting_date<=date_sub('2013-9-13',INTERVAL 1 YEAR),debit,0)),0)- coalesce(sum(if(reporting_date<=date_sub('2013-9-13',INTERVAL 1 YEAR),credit,0)),0),0)as comparative1,
		if(0>0,coalesce(sum(if(reporting_date<=date_sub('2013-9-13',INTERVAL 2 YEAR),debit,0)),0)- coalesce(sum(if(reporting_date<=date_sub('2013-9-13',INTERVAL 2 YEAR),credit,0)),0),0)as comparative2
from gl_trans_detail d 
		inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
		inner join gl_account_types t on (a.acct_type = t.type_name)
		inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
		where h.tenant_id = 1
		and reporting_date <= '2013-9-13'
		group by type_group, type_sub_group, acct_type, acct_code)t
group by type_group, type_sub_group, acct_type, acct_code
order by type_group, type_sub_group, acct_type, acct_code;













DROP PROCEDURE IF EXISTS getBalanceSheet;

DELIMITER $$
CREATE PROCEDURE getBalanceSheet()
BEGIN 
 DECLARE variable1 CHAR(10);
 CREATE TEMPORARY TABLE IF NOT EXISTS mytemp1 ENGINE=MEMORY 
	as (select type_group, type_sub_group, acct_type, acct_code, 
		coalesce(sum(debit),0)- coalesce(sum(credit),0) as amount
		from gl_trans_detail d 
		inner join gl_accounts a on (d.gl_account_id = a.gl_acct_id)
		inner join gl_account_types t on (a.acct_type = t.type_name)
		inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
		where h.tenant_id = 1
		and reporting_date between '2012-9-13' and '2013-9-13'
		group by type_group, type_sub_group, acct_type, acct_code with rollup)  ;

CREATE TEMPORARY TABLE IF NOT EXISTS mytemp2 LIKE mytemp1;

select type_group, type_sub_group, acct_type, acct_code, amount from mytemp1
	where type_group<>'Nominal'
union all
select 'Capital' as type_group, 'Equity' as type_sub_group, 'Retained Earnings' as acct_type, 'Unprocessed' as acct_code, sum(amount) from mytemp2
	where type_group = 'Nominal' and type_sub_group is null
;
END; $$


