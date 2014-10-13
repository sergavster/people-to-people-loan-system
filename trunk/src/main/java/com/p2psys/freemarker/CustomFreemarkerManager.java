package com.p2psys.freemarker;

import javax.servlet.ServletContext;

import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.p2psys.dao.ArticleDao;
import com.p2psys.dao.AttestationDao;
import com.p2psys.dao.LinkageDao;
import com.p2psys.dao.UserDao;
import com.p2psys.freemarker.directive.AreaDirectiveModel;
import com.p2psys.freemarker.directive.AttestationDirectiveModel;
import com.p2psys.freemarker.directive.LinkageDirectiveModel;
import com.p2psys.freemarker.directive.SiteDirectiveModel;
import com.p2psys.freemarker.method.AttestationTypeNameModel;
import com.p2psys.freemarker.method.DateMethodModel;
import com.p2psys.freemarker.method.DateRollMethodModel;
import com.p2psys.freemarker.method.InterestMethodModel;
import com.p2psys.freemarker.method.LinkageMethodModel;
import com.p2psys.freemarker.method.ParserDoubleMethodModel;
import com.p2psys.freemarker.method.ParserLongMethodModel;
import com.p2psys.util.FreemarkerUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class CustomFreemarkerManager extends FreemarkerManager {
	
	@Override  
    protected Configuration createConfiguration(ServletContext servletContext)  
        throws TemplateException {  
        Configuration cfg = super.createConfiguration(servletContext);  
        ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        LinkageDao linkageDao=(LinkageDao)ctx.getBean("linkageDao");
        AttestationDao attestationDao=(AttestationDao)ctx.getBean("attestationDao");
        UserDao userDao=(UserDao) ctx.getBean("userDao");
        ArticleDao articleDao=(ArticleDao) ctx.getBean("articleDao");
        
        
        //计算利息的自定义方法 
        cfg.setSharedVariable("dateformat", new DateMethodModel() );  
        cfg.setSharedVariable("dateroll", new DateRollMethodModel() );  
        cfg.setSharedVariable("interest", new InterestMethodModel() );  
        //新增自定义标签
        cfg.setSharedVariable("linkage", new LinkageDirectiveModel(linkageDao));
        //area标签有冲突，用region代替
        cfg.setSharedVariable("region", new AreaDirectiveModel(linkageDao));

        cfg.setSharedVariable("attestation", new AttestationDirectiveModel(attestationDao));
        cfg.setSharedVariable("Typet", new AttestationTypeNameModel(linkageDao,userDao));
        
        cfg.setSharedVariable("siteDirect", new SiteDirectiveModel(articleDao));
        
        
        cfg.setSharedVariable("getLinkage", new LinkageMethodModel(linkageDao));
        cfg.setSharedVariable("parseDouble", new ParserDoubleMethodModel());
        cfg.setSharedVariable("parseLong", new ParserLongMethodModel());      
        FreemarkerUtil.CONFIG=cfg;
        
        return cfg;  
    }  
}
