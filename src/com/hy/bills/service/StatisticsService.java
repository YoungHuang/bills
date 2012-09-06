package com.hy.bills.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.hy.bills.activity.R;
import com.hy.bills.domain.Bill;
import com.hy.bills.domain.User;

public class StatisticsService {
	private Context context;
	private UserService userService;
	private BillService billService;
	
	public StatisticsService(Context context, UserService userService, BillService billService) {
		this.context = context;
		this.userService = userService;
		this.billService = billService;
	}
	
	public String getBillStatistics(Integer accountBookId) {
		Map<User, Statistics> stats = new HashMap<User, Statistics>();
		List<Bill> billList = billService.findAllByAccountBookId(accountBookId);
		String[] billTypes = context.getResources().getStringArray(R.array.BillType);
		for (Bill bill : billList) {
			List<User> userList = userService.findAllByIds(bill.getUserIds());
			User payUser = userList.get(0);
			String billType = bill.getBillType();
			
			Statistics payStat = stats.get(payUser);
			if (payStat == null) {
				payStat = new Statistics();
				stats.put(payUser, payStat);
			}
			
			if (billType.equals(billTypes[0])) { // 个人
				payStat.perAmount += bill.getAmount().doubleValue();
			} else if (billType.equals(billTypes[1])) { // 均分
				Double amount = bill.getAmount().doubleValue() / userList.size();
				payStat.averAmount += amount;
				
				for (int i = 1; i < userList.size(); i++) {
					User user = userList.get(i);
					
					Double in = payStat.inAmount.get(user);
					if (in == null) {
						in = 0.0;
					}
					in += amount;
					payStat.inAmount.put(user, in);
					
					Statistics stat = stats.get(user);
					if (stat == null) {
						stat = new Statistics();
						stats.put(payUser, stat);
					}
					stat.averAmount += amount;
					Double out = stat.outAmount.get(payUser);
					if (out == null) {
						out = 0.0;
					}
					out += amount;
					stat.outAmount.put(payUser, out);
				}
			}
		}
		
		return constructResult(stats);
	}
	
	private String constructResult(Map<User, Statistics> stats) {
		StringBuilder ret = new StringBuilder();
		for (User user : stats.keySet()) {
			Statistics stat = stats.get(user);
			ret.append(user.getName()).append("\r\n");
			ret.append("个人消费：").append(stat.perAmount).append("元\r\n");
			ret.append("均分消费（个人部分）：").append(stat.averAmount).append("元\r\n");
			ret.append("\r\n");
			for (User oUser : stat.inAmount.keySet()) {
				ret.append("用户").append(oUser.getName()).append("欠款：").append(stat.inAmount.get(user)).append("元\r\n");
			}
			ret.append("\r\n");
			for (User oUser : stat.outAmount.keySet()) {
				ret.append("欠用户").append(oUser.getName()).append("：").append(stat.outAmount.get(user)).append("元\r\n");
			}
			ret.append("\r\n");
		}
			
		return ret.toString();
	}

	private class Statistics {
		// Personal amount
		Double perAmount;
		// Average amount
		Double averAmount;
		Map<User, Double> inAmount;
		Map<User, Double> outAmount;
		
		Statistics() {
			inAmount = new HashMap<User, Double>();
			outAmount = new HashMap<User, Double>();
		}
	}
}
