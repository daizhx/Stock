package com.hengxuan.stock.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalUtil {
	 private static DecimalFormat mAmountFormat = new DecimalFormat(",###,##0.00");
	  
	  public static String amountFormat(BigDecimal paramBigDecimal)
	  {
	    if (paramBigDecimal == null) {}
	    for (String str = "0.00";; str = mAmountFormat.format(paramBigDecimal)) {
	      return str;
	    }
	  }
	  
	  public static String amountFromat(BigDecimal paramBigDecimal)
	  {
	    if (paramBigDecimal == null) {
	      paramBigDecimal = BigDecimal.ZERO;
	    }
	    paramBigDecimal.setScale(2, 1);
	    if (paramBigDecimal.signum() == 0) {}
	    for (String str = "0.00";; str = mAmountFormat.format(paramBigDecimal)) {
	      return str;
	    }
	  }
	  
	  public static String format(double paramDouble)
	  {
	    return mAmountFormat.format(paramDouble);
	  }
	  
	  public static String format(BigDecimal paramBigDecimal)
	  {
	    if (paramBigDecimal == null) {}
	    for (String str = "0.00";; str = paramBigDecimal.setScale(2, 1).toString()) {
	      return str;
	    }
	  }
	  
	  public static String format(BigDecimal paramBigDecimal, int paramInt)
	  {
	    StringBuilder localStringBuilder = null;
	    int i;
	    if (paramBigDecimal == null){
	    	localStringBuilder = new StringBuilder("0.");
	    	return localStringBuilder.toString();
	    }
	    for(i=0;i<paramInt;i++){
	    	paramBigDecimal.setScale(paramInt, 1);
	    	localStringBuilder.append("0");
	    }
	    return localStringBuilder.toString();
	    
	  }
	  
	  public static BigDecimal parse(String paramString)
	  {
	    return new BigDecimal(paramString).setScale(2, 1);
	  }
	  
	  public static int toFen(BigDecimal paramBigDecimal)
	  {
	    return paramBigDecimal.multiply(new BigDecimal(100)).setScale(0, 1).intValueExact();
	  }
	  
	  public static BigDecimal toYuan(int paramInt)
	  {
	    return BigDecimal.valueOf(paramInt).setScale(2, 1).divide(new BigDecimal(100), 1);
	  }
}
