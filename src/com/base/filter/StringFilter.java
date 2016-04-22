package com.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class StringFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		chain.doFilter(new HTMLCharacterRequest(request), resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

// html特殊字符处理类
class HTMLCharacterRequest extends HttpServletRequestWrapper {

	public HTMLCharacterRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return filter(name, super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null || values.length == 0)
			return values;
		for (int i = 0; i < values.length; i++) {
			String str = values[i];
			values[i] = filter(name, str);
		}
		return values;
	}

	/**
	 * 对特殊的html字符进行编码
	 * 
	 * @param message
	 * @return
	 */
	private String filter(String key, String message) {
		if (message == null)
			return null;
		if (key.equals("content") || key.equals("attachmentTempPath")) {
			message = message.replaceAll("<script", "&lt;script");
			message = message.replaceAll("</script>", "&lt;/script&gt;");
			return message;
		} else {
			char content[] = new char[message.length()];
			message.getChars(0, message.length(), content, 0);
			StringBuilder result = new StringBuilder(content.length + 50);
			for (int i = 0; i < content.length; i++) {
				switch (content[i]) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
//				case '&':
//					result.append("&amp;");
//					break;
//				case '"':
//					result.append("&quot;");
//					break;
				default:
					result.append(content[i]);
				}
			}
			return (result.toString());
		}
	}
}
