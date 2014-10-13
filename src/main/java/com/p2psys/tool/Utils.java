package com.p2psys.tool;

import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.tool.interest.MonthInterestCalculator;


public class Utils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		InterestCalculator ic =new MonthInterestCalculator(100,0.12,3);
		ic.each();
		//System.out.println(ic.toString());
	}
	
	/**
	 * 
	 * @param p 本金
	 * @param r 月利率
	 * @param n 还款月数
	 * @return  每月还款金额
	 */

	public static double Mrpi(double p,double r,int mn){
		double mr=r/12;
		
		double aprPow=Math.pow(1+mr,mn);
		
		double monPay=p*mr*aprPow/(aprPow-1);
		
		return monPay;
		
	}
	/**
     * 投资人
     * @param p 本金
     * @param r 月利率
     * @param n 还款月数
     * @return  每月还款金额
     */

    public static double NewMrpi(double p,double r,int mn){
        double mr=r;
        
        double aprPow=Math.pow(1+mr,mn);
        
        double monPay=p*mr*aprPow/(aprPow-1);
        
        return monPay;
        
    }
	
	/**
	 * 
	 * 三个变量x,y,z  
     *      x代表提现金额 y代表现在净资产减15天内的充值总值z代表提现手续费
     *      1.  0≤x ≤1500 或者 y<15000
     * 无论y为何值    z=0.002x
     *       2.  y≥x
     *      1500<x ≤30000     z=3
     *       30000<x ≤50000    z=5
     *       3. y <x
     *       1500<y ≤30000     z=3+(x-y)0.002
     *       30000<y ≤50000    z=5+(x-y)0.002
	 * 
	 * 
	 * @param x x代表提现金额 
	 * @param y y免费额度
	 * @param r r代表提现手续费率
	 * @param maxCash
	 * @return 提现手续费
	 */
	public static double GetCashFee(double x,double y,double r,double maxCash){
		if(x<=1500||y<=1500){
			return r*x;
		}else if(y>=x){
			if(x>1500&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	
	public static double GetCashFeeForAidai(double x,double y,double r,double maxCash){
		if (y<=0) {
			return 0;
		} else if (y<=x) {
			return x*r;
		} else {
			return (x-y)*r;
		}
	}
	
	/**
	 * 招商贷体现规则（充值15天内取现千三手续费，15天后免费）
	 * y：免费提现额度
	 * x：提现金额
	 */
	public static double cashFeeForZsd(double x,double y,double r,double maxCash){
		if(y<=0){
			return 0;
		} else {
			return r*x;
		}
	}
	
	/**
	 * 招商贷提现规则
	 */
	public static double GetZsdCashFee(double x,double y,double r,double maxCash){
		if (y <= 0) {
			return r * x;
		} else if (y >= x) {
			return 0;
		} else {
			return (x - y) * r;
		}
	}
	
	/**
	 * 和信贷提现规则
	 * 指定日期前提现收取相应比例提现手续费，不足最低收费标准按最低收费标准收取；指定日期后提现免费；
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @return
	 */
	public static double getCashFeeForHeXinDai(double x,double y,double r,double maxCash,double lowestCashFee){
		double cashFee = 0;
		if(y <= 0){
			cashFee = r*x;
		}else if (y >= x){
			cashFee = 0;
		} else{
			cashFee = (x -y) * r;
		}
		if(cashFee >= lowestCashFee || (y >= x)){
			return cashFee;
		}{
			return lowestCashFee;
		}
	}
	/**
	 * 莲花财富提现规则
	 */
	public static double getCashFeeForlhd(double x,double y,double r,double maxCash){
		 if(y>=x){
				return 0 ;
		}else {
			if(y > 0){
				return (x-y) * r;
			}else {
				return x * r;
			}
		}
	}
	/**
	 * 中融资本的提现费用计算公式
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @return
	 */
	public static double getCashFeeForZRZB(double x,double y,double r,double maxCash){
		if(x<=1500||y<=1500){
			return r*x;
		}else if(y>=x){
			if(x>1500&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 及时雨提现规则
	 */
	public static double getCashFeeForJJY(double x,double y,double r,double maxCash){
		/*if(x<y){
			if (x>=0) {
				return (y-x)*r;
			}else {
				return y*r;
			}
		}else{
			return 0;
		}*/
		if(y>=x){
			if(x>0&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 山水聚宝提现规则
	 */
	public static double getCashFeeForSSJB(double x,double y,double r,double maxCash,double tender_award){
		 if(y>=x||tender_award>=x){
			return 0;
		}else{
			if(y>0){
				if(tender_award>0&&tender_award<x&&x-y-tender_award>0){
				    return (x-y-tender_award)*r;
				}else{
					return (x-y)*r;
				}
			}else{
				return x*r;
			}
		}
	}
	/**
	 * 融易贷提现规则
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @param tender_award
	 * @return
	 */
	public static double getCashFeeForRYD(double x,double r,double money){
		return money*r;
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @return
	 */
	public static double getCashFeeForZrzbZero(double x,double y,double r,double maxCash){
		if(y>=x){
			if(x>1500&&x<=30000){
				return 0;
			}else{
				return 0;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 中融资本新提现规则  
	 * @param x
	 * @param r
	 * @param money
	 * @return
	 */
	public static double getCashFeeForZrzbZero(double x,double r,double money){
		if(x<money){
			if (x>=0) {
				return (money-x)*r;
			}else {
				return money*r;
			}
		}else{
			return 0;
		}
	}
	
	public static double GetLargeCashFee(double x,double y,double r,double maxCash){
		if(y>=500000){
			return x/10000;
		}else if(y<20000){
			if(y<0){
				y=0;
			}
			return y/10000+(x-y)*r;
		}else{
			if(y>=x){
				return x/10000;
			}else{
				return y/10000+(x-y)*r;
			}
		}
	}
	
	/**
	 * 徽贷提现规则  
	 * @param x
	 * @param r
	 * @param money
	 * @return
	 */
	public static double getCashFeeForHuidai(double money,double x,double r){
		if(x<money){
			if (x>=0) {
				return (money-x)*r;
			}else {
				return money*r;
			}
		}else{
			return 0;
		}
	}
	/**
	 * 河南贷提现规则
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @param t
	 * @param o
	 * @param or
	 * @return
	 */
	public static double getCashFeeForHndai(double x, double y, double r,
			double maxCash, double t, double o, double or) {
			double todayCashFee = 0;
			if(t>0){
				if (o > t) {
					todayCashFee = 0;
				}else{
					if(x>t){
						todayCashFee = (t-o)*or;
					}else{
						if(x>t-o){
							todayCashFee=(t-o)*or;
						}else{
							todayCashFee=x*or;
						}
					}
				}
			}else{
				todayCashFee = 0;
			}
			
			if (y >= x && todayCashFee==0) {
				return 3;
			} else if(y >=x && todayCashFee>0){
				return todayCashFee;
			} else {
				if (y > 0) {
					return (x - y) * r + todayCashFee;
				} else {
					return x * r + todayCashFee;
				}
			}
	}
	
	/**
	 * 国临创投提现规则
	 * @param x
	 * @param r
	 * @param money
	 * @return
	 */
	public static double getCashFeeForJlct(double x, double r, double money,double lastsum) {
		//超出免费额度的部分
		double y=money-x;
		//费率
		double fee =0;
		if (x < money) {
			if (x >= 0) {
				if(lastsum>0){
					if(y<=lastsum){
						fee=y*r;
					}else if(y>lastsum){
						fee=lastsum*r;
					}
				}
				return fee;
			} else {
				if(lastsum>0){
					if(money<=lastsum){
						fee=money*r;
					}else if(money>lastsum){
						fee=lastsum*r;
					}
				}
				return fee;
			}
		} else {
			return 0;
		}
	}
}

