package com.p2psys.tool.jxl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.ReflectUtils;

public class ExcelHelper {

	public static final String UID="serialVersionUID"; 
	private static Logger logger = Logger.getLogger(ExcelHelper.class);
	
	public static void writeExcel(String file,List list,Class clazz) throws Exception {
		Field[] fields=clazz.getDeclaredFields();
		List flist=new ArrayList();
		for(int i=0;i<fields.length;i++){
			if(fields[i].getName().equals(UID)){
				continue;
			}
			flist.add(fields[i].getName());
		}
		writeExcel(file,list,clazz,flist,null);
	}
	
	public static void writeExcel(String file,List list,Class clazz,List<String> fields,List<String> titles) throws Exception {
		OutputStream os=getOutputStream(file);
		jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(os);
		jxl.write.WritableSheet ws = wwb.createSheet("Sheet1", 0);
		jxl.write.Label label=null;
		int start=0;
		if(titles!=null&&titles.size()>0){
			for(int j=0;j<titles.size();j++){ 
				label=new jxl.write.Label(j,0,titles.get(j));
				ws.addCell(label);
			}
			start++;
		}
		for(int i=start;i<list.size()+start;i++){
			Object o=list.get(i-start);
			if(o==null){
				continue;
			}
			for(int j=0;j<fields.size();j++){
				String value="";
				String field = fields.get(j);
				if(field == null || field.equals("serialVersionUID")){
					continue;
				}
				try {
					value=ReflectUtils.invokeGetMethod(clazz, o, field).toString();
				} catch (Exception e) {
					
				}
				if(field!=null&&isTime(field)){
					if(value.isEmpty()){
						value="";
					}else{
						value=DateUtils.dateStr4(value);
					}
				}
				//判断是否包含金钱，如有，将其保留两位有效数字
				if(field!=null&&isMoney(field)){
					if(value.isEmpty()){
						value="";
					}else{
						value=NumberUtils.format2Str(NumberUtils.getDouble(value));
					}
				}
				label=new jxl.write.Label(j,i, value); 
				ws.addCell(label);
			}
		}
		wwb.write();
		wwb.close();
	}
	
	public static List[] read(String xls) throws Exception {  
		List[] data=null; 
		File file=new File(xls);
		if(file.exists()){
			data=read(file);
		}
		return data;
	}
	
	public static List[] read(File file) throws Exception {  
		List[] data=null; 
        Workbook wb = null;  
        try {  
            wb = Workbook.getWorkbook(file);  
            if (wb != null) {  
                Sheet[] sheets = wb.getSheets();
                if (sheets != null && sheets.length >= 0) {  
                	int rows = sheets[0].getRows(); 
                	data = new List[rows]; 
                    for (int j=0;j<rows;j++) {  
                    	List<String> rowData=new ArrayList();
                        Cell[] cells = sheets[0].getRow(j);  
                        if (cells != null && cells.length != 0) { 
                            for (int k=0;k<cells.length;k++) {  
                                String cell = cells[k].getContents();
                                rowData.add(cell);
                            } 
                        }  
                        data[j]=rowData;
                    } 
                } 
            }  
        } catch (Exception e) {  
        	logger.info(e.getMessage());
        } finally {  
            wb.close();  
        }  
        return data;  
    }  
	
	
	private static boolean isTime(String field){
		// v1.6.6.2 RDPROJECT-277 xx 2013-10-24 start
		String[] times = new String[] { "addtime", "addTime", "repay_time", "verify_time", "repay_yestime",
				"repayment_time", "repayment_yestime", "registertime", "vip_verify_time", "full_verifytime",
				"last_tender_time","kefu_addtime",
		// v1.6.7.1 RDPROJECT-384 wcw 2013-11-20 start
				"realname_verify_time","video_verify_time","scene_verify_time",
				"phone_verify_time","pwd_modify_time","vip_end_time",
		// v1.6.7.1 RDPROJECT-384 wcw 2013-11-20 end
		 "add_time","update_time","interest_start_time","interest_end_time"};
		boolean isTime=false;
		for(String s:times){
			if(s.equals(field)){
				isTime=true;
				break;
			}
		}
		return isTime;
	}
	/**
	 * 判断是否是金钱
	 * @param field
	 * @return
	 */
	private static boolean isMoney(String field){
		String[] money = new String[]{"sum","use_money","collection","total","no_use_money","money"};
		boolean isMoney = false;
		for(String s:money){
			if(s.equals(field)){
				isMoney = true;
				break;
			}
		}
		return isMoney;
	}
	
	public static OutputStream getOutputStream(String file) throws Exception{
		File f = new File(file);
		f.createNewFile();
		OutputStream os=new FileOutputStream(f);
		return os;
	}

}
