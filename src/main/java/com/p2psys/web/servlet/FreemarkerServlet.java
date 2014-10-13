package com.p2psys.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.p2psys.dao.AttestationDao;
import com.p2psys.dao.LinkageDao;
import com.p2psys.freemarker.directive.AreaDirectiveModel;
import com.p2psys.freemarker.directive.AttestationDirectiveModel;
import com.p2psys.freemarker.directive.LinkageDirectiveModel;
import com.p2psys.freemarker.method.DateMethodModel;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.IncludePage;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FreemarkerServlet extends freemarker.ext.servlet.FreemarkerServlet {
	private static Logger logger = Logger.getLogger(FreemarkerServlet.class);
	private static final long serialVersionUID = -8234001005016304634L;

	// a freemarker script.
    private static final String ATTR_REQUEST_MODEL = ".freemarker.Request";
    private static final String ATTR_REQUEST_PARAMETERS_MODEL = ".freemarker.RequestParameters";
    private static final String ATTR_SESSION_MODEL = ".freemarker.Session";
    private static final String ATTR_APPLICATION_MODEL = ".freemarker.Application";
    private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
	
	private ObjectWrapper wrapper=ObjectWrapper.DEFAULT_WRAPPER;
	
	private WebApplicationContext ctx;
	private LinkageDao linkageDao; 
	private AttestationDao attestationDao;


	public FreemarkerServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map data = new HashMap();
		String requestUri = request.getRequestURI();
		String contextUri = this.getServletConfig().getServletContext()
				.getRealPath("/");
		contextUri = contextUri.replace("\\", "/");
		if (contextUri.endsWith("/")) {
			contextUri = contextUri.substring(0, contextUri.length() - 1);
		}
		String context = request.getContextPath();
		contextUri = contextUri.replaceFirst(context, "");
		String templatePath = requestUri.replaceFirst(context, "");
		String targetPath = contextUri + requestUri;
		if (!File.separator.equals("/")) {
			targetPath = targetPath.replace("/", "\\");
		}
		crateHTML(request, data, templatePath, response);
	}

	public void crateHTML(HttpServletRequest request, Map data,
			String templatePath, HttpServletResponse response) {
		Configuration freemarkerCfg = new Configuration();
		
		addDirectiveModel(freemarkerCfg);
		
		freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
		// 加载模版
		freemarkerCfg.setServletContextForTemplateLoading(request.getSession()
				.getServletContext(), "/");
		freemarkerCfg.setEncoding(Locale.getDefault(), "UTF-8");
		try {
			// 指定模版路径
			Template template = freemarkerCfg
					.getTemplate(templatePath, "UTF-8");
			template.setEncoding("UTF-8");
			// 页面输出
			response.setContentType("text/html;charset=UTF-8");
			Writer out = response.getWriter();

			
			Object o = request.getSession().getAttribute("session_user");
			logger.info("Session:" + o);
			//data.put("session", request.getSession());
			ServletContext servletContext = getServletContext();
			TemplateModel model = createModel(wrapper, servletContext, request,
					response);
			// 处理模版
			template.process(model, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				request.setAttribute("exception", e.getMessage());
				request.setAttribute("exceptionStack", e);
				request.getRequestDispatcher("/404.jsp").forward(request,
						response);
				
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		ctx=WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		linkageDao=(LinkageDao)ctx.getBean("linkageDao");
		attestationDao=(AttestationDao)ctx.getBean("attestationDao");
	}

	protected TemplateModel createModel(ObjectWrapper wrapper,
			ServletContext servletContext, final HttpServletRequest request,
			final HttpServletResponse response) throws TemplateModelException {
		try {
			AllHttpScopesHashModel params = new AllHttpScopesHashModel(wrapper,
					servletContext, request);

			// Create hash model wrapper for servlet context (the application)
			ServletContextHashModel servletContextModel = (ServletContextHashModel) servletContext
					.getAttribute(ATTR_APPLICATION_MODEL);
			if (servletContextModel == null) {
				servletContextModel = new ServletContextHashModel(this, wrapper);
				servletContext.setAttribute(ATTR_APPLICATION_MODEL,
						servletContextModel);
				TaglibFactory taglibs = new TaglibFactory(servletContext);
				servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);
				initializeServletContext(request, response);
			}
			
			params.putUnlistedModel(KEY_APPLICATION, servletContextModel);
			params.putUnlistedModel(KEY_APPLICATION_PRIVATE,
					servletContextModel);
			params.putUnlistedModel(KEY_JSP_TAGLIBS,
					(TemplateModel) servletContext
							.getAttribute(ATTR_JSP_TAGLIBS_MODEL));
			// Create hash model wrapper for session
			HttpSessionHashModel sessionModel;
			HttpSession session = request.getSession(false);
			if (session != null) {
				sessionModel = (HttpSessionHashModel) session
						.getAttribute(ATTR_SESSION_MODEL);
				if (sessionModel == null ) {//  || sessionModel.isOrphaned(session)
					sessionModel = new HttpSessionHashModel(session, wrapper);
					session.setAttribute(ATTR_SESSION_MODEL, sessionModel);
			        initializeSession(request, response);
				}
			} else {
				sessionModel = new HttpSessionHashModel(this, request,
						response, wrapper);
			}
			params.putUnlistedModel(KEY_SESSION, sessionModel);

			// Create hash model wrapper for request
			HttpRequestHashModel requestModel = (HttpRequestHashModel) request
					.getAttribute(ATTR_REQUEST_MODEL);
			if (requestModel == null || requestModel.getRequest() != request) {
				requestModel = new HttpRequestHashModel(request, response,
						wrapper);
				request.setAttribute(ATTR_REQUEST_MODEL, requestModel);
				request.setAttribute(ATTR_REQUEST_PARAMETERS_MODEL,
						createRequestParametersHashModel(request));
			}
			params.putUnlistedModel(KEY_REQUEST, requestModel);
			params.putUnlistedModel(KEY_INCLUDE, new IncludePage(request,
					response));
			params.putUnlistedModel(KEY_REQUEST_PRIVATE, requestModel);

			// Create hash model wrapper for request parameters
			HttpRequestParametersHashModel requestParametersModel = (HttpRequestParametersHashModel) request
					.getAttribute(ATTR_REQUEST_PARAMETERS_MODEL);
			params.putUnlistedModel(KEY_REQUEST_PARAMETERS,
					requestParametersModel);
			//自定义变量或者函数
			
			params.put("request", request);
			
			return params;
		} catch (ServletException e) {
			throw new TemplateModelException(e);
		} catch (IOException e) {
			throw new TemplateModelException(e);
		}
	}
	
	protected void setCustomMethodOrVariable(AllHttpScopesHashModel params){
		params.put("dateformat", new DateMethodModel());
	}
	
	//增加自定义指令
	protected void addDirectiveModel(Configuration cfg){
		//借款用途的下拉框
		cfg.setSharedVariable("linkage", new LinkageDirectiveModel(linkageDao));
		//地区的下拉框，area标签有冲突，用region代替
		cfg.setSharedVariable("region", new AreaDirectiveModel(linkageDao));
		cfg.setSharedVariable("attestation", new AttestationDirectiveModel(attestationDao));
        
	}
}
