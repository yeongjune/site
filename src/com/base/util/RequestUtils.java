package com.base.util;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Created by dzf on 16-1-18.
 */
public class RequestUtils {

    /**
     * 判断谁否为手机浏览
     * @return
     */
    public static boolean isWap(HttpServletRequest request){
        String header = request.getHeader("user-agent");
        if(StringUtil.isEmpty(header)) header = request.getHeader("USER-AGENT");
        if(StringUtil.isEmpty(header)) header = request.getHeader("User-Agent");
        String regex = "^((.*MIDP.*)|(.*WAP.*)|(.*UP.Browser.*)|(.*Smartphone.*)|(.*Obigo.*)|(.*Mobile.*)" +
                "|(.*AU.Browser.*)|(.*wxd.Mms.*)|(.*WxdB.Browser.*)|(.*CLDC.*)|(.*UP.Link.*)|(.*KM.Browser.*)" +
                "|(.*UCWEB.*)|(.*SEMC\\-Browser.*)|(.*Mini.*)|(.*Symbian.*)|(.*Palm.*)|(.*Nokia.*)|(.*Panasonic.*)" +
                "|(.*MOT\\-.*)|(.*SonyEricsson.*)|(.*NEC\\-.*)|(.*Alcatel.*)|(.*Ericsson.*)|(.*BENQ.*)|(.*BenQ.*)" +
                "|(.*Amoisonic.*)|(.*Amoi\\-.*)|(.*Capitel.*)|(.*PHILIPS.*)|(.*SAMSUNG.*)|(.*Lenovo.*)|(.*Mitsu.*)" +
                "|(.*Motorola.*)|(.*SHARP.*)|(.*WAPPER.*)|(.*LG\\-.*)|(.*LG/.*)|(.*EG900.*)|(.*CECT.*)|(.*Compal.*)" +
                "|(.*kejian.*)|(.*Bird.*)|(.*BIRD.*)|(.*G900/V1.0.*)|(.*Arima.*)|(.*CTL.*)|(.*TDG.*)|(.*Daxian.*)" +
                "|(.*DAXIAN.*)|(.*DBTEL.*)|(.*Eastcom.*)|(.*EASTCOM.*)|(.*PANTECH.*)|(.*Dopod.*)|(.*Haier.*)" +
                "|(.*HAIER.*)|(.*KONKA.*)|(.*KEJIAN.*)|(.*LENOVO.*)|(.*Soutec.*)|(.*SOUTEC.*)|(.*SAGEM.*)" +
                "|(.*SEC\\-.*)|(.*SED\\-.*)|(.*EMOL\\-.*)|(.*INNO55.*)|(.*ZTE.*)|(.*iPhone.*)|(.*Android.*)" +
                "|(.*Windows CE.*)|(Wget.*)|(java.*)|(curl.*)|(Opera.*))$";
        return Pattern.matches(regex, header);
    }

}
