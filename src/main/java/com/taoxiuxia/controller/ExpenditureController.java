package com.taoxiuxia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taoxiuxia.model.Balance;
import com.taoxiuxia.model.Expenditure;
import com.taoxiuxia.model.Item;
import com.taoxiuxia.model.SessionUser;
import com.taoxiuxia.service.IExpenditureService;
import com.taoxiuxia.service.IItemService;
import com.taoxiuxia.service.IMonthlyStatisticsService;
import com.taoxiuxia.util.Constants;
import com.taoxiuxia.util.MyDateFormat;
import com.taoxiuxia.util.NumberFormat;

@Controller
@RequestMapping("/expenditureController")
public class ExpenditureController {

	private IExpenditureService expenditureService;
	private IItemService itemService;
	private IMonthlyStatisticsService monthlyStatisticsService;

	public IExpenditureService getExpenditureService() {
		return expenditureService;
	}

	@Autowired
	public void setExpenditureService(IExpenditureService expenditureService) {
		this.expenditureService = expenditureService;
	}

	public IItemService getItemService() {
		return itemService;
	}

	@Autowired
	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public IMonthlyStatisticsService getMonthlyStatisticsService() {
		return monthlyStatisticsService;
	}

	@Autowired
	public void setMonthlyStatisticsService(IMonthlyStatisticsService monthlyStatisticsService) {
		this.monthlyStatisticsService = monthlyStatisticsService;
	}
	
	
	/**
	 * expenditure页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showExpenditure")
	public String showExpenditures(Model model,HttpSession session) {
		Map<String, Float> map = monthlyStatistics(session);

		//页面上的统计信息
		model.addAttribute("totalIncome", NumberFormat.save2Decimals(map.get("monthlyIncome")));
		model.addAttribute("totalExpenditure", NumberFormat.save2Decimals(map.get("monthlyExpenditure")));
		model.addAttribute("huaBeiAndCreditCard", NumberFormat.save2Decimals(map.get("huaBeiAndCreditCard")));
		model.addAttribute("balanceInBeginOfMonth", NumberFormat.save2Decimals(map.get("balanceInBeginOfMonth")));
		model.addAttribute("balanceShould", NumberFormat.save2Decimals(map.get("balanceShould")));
		model.addAttribute("actualBalance", NumberFormat.save2Decimals(map.get("actualBalance")));
		model.addAttribute("actualExpenditure", NumberFormat.save2Decimals(map.get("actualExpenditure")));
		
		// Expenditure list
		List<Expenditure> expenditures = expenditureService.loadExpenditures();
		model.addAttribute("expenditures", expenditures);

		// Expenditure项 list 
		List<Item> items = itemService.loadExpenditureItems(2); // 目前只有用户2
		model.addAttribute("items", items);
		
		SessionUser sessionUser= (SessionUser) session.getAttribute(Constants.SESSION_USER_KEY);
		model.addAttribute("sessionUser", sessionUser);

		return "pages/expenditure";
	}

	/**
	 * 增加Expenditures
	 * 
	 * @param item
	 * @param money
	 * @param remark
	 */
	@RequestMapping("/addExpenditure")
	public void addExpenditures(String date, int item, float money, String moneyType, String remark) {
		expenditureService.addExpenditure(date, item, money, moneyType, remark);
	}

	/**
	 * 修改Expenditures
	 * 
	 * @param ExpendituresId
	 * @param money
	 * @param itemId
	 * @param remark
	 */
	@RequestMapping("/changeExpenditure")
	public void changeExpenditures(int expenditureId, float money, String moneyType, int itemId, String remark,String date) {
		expenditureService.changeExpenditure(expenditureId, money, moneyType, itemId, remark,MyDateFormat.dateFormat(date));
	}

	/**
	 * 删除Expenditures
	 * 
	 * @param ExpenditureId
	 * @param itemId
	 */
	@RequestMapping("/deleExpenditure")
	public void deleExpenditure(int expenditureId, int itemId) {
		expenditureService.deleExpenditure(expenditureId, itemId);
	}
	
	/**
	 * 在收入和支出页面上 统计部分的内容
	 * @param session
	 * @return
	 */
	public Map<String,Float> monthlyStatistics(HttpSession session) {
		Map<String,Float>map = new HashMap<String, Float>();
		
		// 月收入
		float monthlyIncome = monthlyStatisticsService.monthlyIncome(2); 
		map.put("monthlyIncome", monthlyIncome);
		
		// 月支出
		float monthlyExpenditure = monthlyStatisticsService.monthlyExpenditure(2);  
		map.put("monthlyExpenditure", monthlyExpenditure);
		
		// 月支出中花呗与信用卡的数额
		float huaBeiAndCreditCard = monthlyStatisticsService.huaBeiAndCreditCard(2);
		map.put("huaBeiAndCreditCard", huaBeiAndCreditCard);
		
		// 本月实际支出 
		float actualExpenditure = monthlyExpenditure - huaBeiAndCreditCard;
		map.put("actualExpenditure", actualExpenditure);
		
		// 本月初（上月末）结余
		float balanceInBeginOfMonth = monthlyStatisticsService.balanceInBeginOfMonth(2);
		map.put("balanceInBeginOfMonth", balanceInBeginOfMonth);
		
		// 本月应结余 ==> 月初结余+月收入- (月支出-花呗/信用卡)
		float balanceShould = balanceInBeginOfMonth + monthlyIncome - (monthlyExpenditure - huaBeiAndCreditCard);
		map.put("balanceShould", balanceShould);
		
		// 本月实际结余
		Balance balanceOfThisMonth = monthlyStatisticsService.balanceOfThisMonth(2);
		float actualBalance; // 本月实际结余
		if(balanceOfThisMonth == null){
			actualBalance = -1;
		}else{
			actualBalance = balanceOfThisMonth.getActualBalance();
		}
		map.put("actualBalance", actualBalance);
		
		return map;
	}
}
