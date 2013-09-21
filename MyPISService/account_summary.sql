select m.year_, m.month_, m.period, sum(DEBIT) as debit, sum(CREDIT) as credit from (
select year(h.reporting_date) as year_, month(h.reporting_date) as month_, 
		sum(DEBIT) as debit, sum(CREDIT) as credit
		from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)	
where gl_account_id = 75
and reporting_date between '2013-01-01' and '2013-09-30'
group by year(h.reporting_date),month(h.reporting_date))v
	right join (select YEAR(adate) year_, month(aDate) as month_, date_format(aDate,'%M %Y') as period from (
		  select @endDate - interval (a.a + (10 * b.a) + (100 * c.a)) month as aDate from
		  (select 0 as a union all select 1 union all select 2 union all select 3
		   union all select 4 union all select 5 union all select 6 union all
		   select 7 union all select 8 union all select 9) a,
		  (select 0 as a union all select 1 union all select 2 union all select 3
		   union all select 4 union all select 5 union all select 6 union all
		   select 7 union all select 8 union all select 9) b,
		  (select 0 as a union all select 1 union all select 2 union all select 3
		   union all select 4 union all select 5 union all select 6 union all
		   select 7 union all select 8 union all select 9) c,
		  (select @startDate := '2013-01-01', @endDate := '2013-09-30') d
		) e where aDate between @startDate and @endDate)m 
		on (v.year_ = m.year_ and v.month_ = m.month_)
group by m.year_, m.month_, m.period;

select reporting_date, trans_type, voucher_serial, my_trans_ref, d.description, DEBIT, CREDIT
		from gl_trans_detail d inner join gl_trans_header h on (d.gl_trans_header_id=h.gl_trans_header_id)
where gl_account_id = 75
and h.tenant_id = 1
and reporting_date between '2013-1-1' and '2013-9-30'
