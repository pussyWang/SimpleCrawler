package com.wkself.demo.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpUtil {
	static transient Log logger = LogFactory.getLog(HttpUtil.class);

	public static final String DEFALT_MIME_TYPE = "text/plain";
	public static final String DEFALT_CHARSET_STR = "UTF-8";
	public static final ContentType DEFALT_CONTENT_TYPE = ContentType.create(
			DEFALT_MIME_TYPE, DEFALT_CHARSET_STR);

	private PoolingHttpClientConnectionManager poolConnMgr;
	private Map<HttpRoute, Integer> maxPerRouatMap;

	private ContentType contentType = DEFALT_CONTENT_TYPE;
	private Boolean disableContentCompression;
	private Long keepAliveDuration;// Http1.1默认开启KeepAlive

	private CloseableHttpClient _client;

	/**
	 * 
	 * @param requestUri
	 *            URL
	 * @return
	 */
	public String get(String requestUri) {
		return get(requestUri, null);
	}

	/**
	 * 
	 * @param requestUri
	 * @param config
	 *            主要用于指定代理和超时时间.
	 * @return
	 */
	public String get(String requestUri, RequestConfig config) {
		return get(requestUri, null, config);
	}

	public String get(String requestUrl, List<NameValuePair> params,
			RequestConfig config) {
		return get(requestUrl, params, null, config);
	}
	
	/**
	 * 
	 * @param requestUrl
	 * @param params
	 * @param config
	 *            主要用于指定代理和超时时间.
	 * @return
	 */
	public String get(String requestUrl, List<NameValuePair> params,Map<String, String> headers,
			RequestConfig config) {
		try {
			CloseableHttpClient httpClient = getHttpClientInstance();
			String url = structGetUrl(requestUrl, params);
			HttpGet httpGet = structHttpGet(url, config);
			if(headers  != null){
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpGet.addHeader(entry.getKey(), entry.getValue());
				}
			}
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				return extractResult(response);
			} finally {
				response.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getWithHeaders(String url, Map<String, String> headers) {
		return get(url,null,headers,null);
	}

	/**
	 * 
	 * 
	 * @param requestUri
	 * @param requestStr
	 * @param config
	 *            主要用于指定代理和超时时间.不需要则设为null.
	 * @return
	 */
	public String post(String requestUri, String requestStr,
			RequestConfig config) {
		return post(requestUri, new StringEntity(requestStr, contentType),
				config);
	}

	/**
	 * 
	 * @param requestUri
	 * @param nameValuePairs
	 * @param config
	 *            主要指定代理和超时时间.不需要则设为null.
	 * @return
	 */
	public String post(String requestUri, List<NameValuePair> nameValuePairs,
			RequestConfig config) {
		UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
				nameValuePairs, contentType.getCharset());
		return post(requestUri, requestEntity, config);
	}

	public String post(String requestUri, HttpEntity requestEntity,
			RequestConfig config) {
		try {
			CloseableHttpClient httpClient = getHttpClientInstance();
			HttpPost httpPost = structHttpPost(requestUri, requestEntity,
					config);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				return extractResult(response);
			} finally {
				response.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 下载文件内容到内存中. 文件内容大小超过 512M 抛出异常.
	 * 
	 * @param requestUri
	 * @param params
	 * @param config
	 *            主要指定代理和超时时间.不需要则设为null.
	 * @return
	 */
	public byte[] requestDataByGet(String requestUri,
								   List<NameValuePair> params, RequestConfig config) {
		String url = structGetUrl(requestUri, params);
		HttpGet get = structHttpGet(url, config);
		return requestData(get);
	}

	private String structGetUrl(String requestUri, List<NameValuePair> params) {
		if (params == null || params.size() == 0) {
			return requestUri;
		}
		String paramStr = encodeGetParams(params);
		if (requestUri.contains("?") && !requestUri.endsWith("?")) {
			paramStr = "&" + paramStr;
		} else {
			paramStr = "?" + paramStr;
		}
		requestUri = requestUri + paramStr;
		return requestUri;
	}

	private String encodeGetParams(List<NameValuePair> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		try {
			StringBuilder sb = new StringBuilder();
			for (NameValuePair pair : params) {
				sb.append("&");
				sb.append(pair.getName());
				sb.append("=");
				sb.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			}
			sb.deleteCharAt(0);
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 下载文件内容到内存中. 文件内容大小超过 512M 抛出异常.
	 * 
	 * @param requestUri
	 * @param nameValuePairs
	 * @param config
	 *            主要指定代理和超时时间.不需要则设为null.
	 * @return
	 */
	public byte[] requestDataByPost(String requestUri,
									List<NameValuePair> nameValuePairs, RequestConfig config) {
		UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(
				nameValuePairs, contentType.getCharset());
		HttpPost httpPost = structHttpPost(requestUri, requestEntity, config);
		return requestData(httpPost);
	}

	/**
	 * 下载文件内容到内存中. 文件内容大小超过 512M 抛出异常.
	 * 
	 * @param request
	 * @return
	 */
	public byte[] requestData(HttpUriRequest request) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			CloseableHttpClient httpClient = getHttpClientInstance();
			CloseableHttpResponse response = httpClient.execute(request);
			try {
				HttpEntity responseEntity = response.getEntity();
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode < 200 || statusCode >= 300) {
					throw new RuntimeException(
							"http return status code error, statusCode:"
									+ statusCode + ",detail:" + responseEntity == null ? "none"
									: EntityUtils.toString(responseEntity));
				}

				if (responseEntity != null) {
					if (responseEntity.isStreaming()) {
						if (responseEntity.getContentLength() > 512 * 1024 * 1024) {
							throw new RuntimeException(
									"Error! Http util try to download one file more than 512M!");
						}
						responseEntity.writeTo(data);
					}
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return data.toByteArray();
	}

	private HttpGet structHttpGet(String requestUri, RequestConfig config) {
		HttpGet httpGet = new HttpGet(requestUri);
		httpGet.setConfig(config);
		return httpGet;
	}

	private HttpPost structHttpPost(String requestUri, HttpEntity entity,
									RequestConfig config) {
		HttpPost httpPost = new HttpPost(requestUri);
		httpPost.setEntity(entity);
		httpPost.setConfig(config);
		logger.debug(httpPost);
		return httpPost;
	}

	private String extractResult(CloseableHttpResponse response)
			throws IOException {
		String result = null;
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			result = EntityUtils.toString(responseEntity);
			EntityUtils.consume(responseEntity);
		}
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode < 200 || statusCode >= 300) {
			throw new RuntimeException(
					"http return status code error, statusCode:" + statusCode
							+ ",detail:" + result);
		}
		return result;
	}

	private CloseableHttpClient getHttpClientInstance() {
		if (_client != null) {
			return _client;
		}
		synchronized (this) {
			if (_client != null) {
				return _client;
			}
			HttpClientBuilder clientBuilder = HttpClients.custom();
			if (disableContentCompression != null && disableContentCompression) {
				clientBuilder.disableContentCompression();
			}
			if (keepAliveDuration != null) {
				clientBuilder
						.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
							public long getKeepAliveDuration(
									HttpResponse response, HttpContext context) {
								return keepAliveDuration;
							}
						});
			}
			if (poolConnMgr != null) {
				if (maxPerRouatMap != null) {
					for (Map.Entry<HttpRoute, Integer> entity : maxPerRouatMap
							.entrySet()) {
						poolConnMgr.setMaxPerRoute(entity.getKey(),
								entity.getValue());
					}
				}
				clientBuilder.setConnectionManager(poolConnMgr);
			}
			_client = clientBuilder.build();
			logger.debug(_client);
		}
		return _client;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Boolean getDisableContentCompression() {
		return disableContentCompression;
	}

	public void setDisableContentCompression(Boolean disableContentCompression) {
		this.disableContentCompression = disableContentCompression;
	}

	public Long getKeepAliveDuration() {
		return keepAliveDuration;
	}

	public void setKeepAliveDuration(Long keepAliveDuration) {
		this.keepAliveDuration = keepAliveDuration;
	}

	public PoolingHttpClientConnectionManager getPoolConnMgr() {
		return poolConnMgr;
	}

	public void setPoolConnMgr(PoolingHttpClientConnectionManager poolConnMgr) {
		this.poolConnMgr = poolConnMgr;
	}

	public Map<HttpRoute, Integer> getMaxPerRouatMap() {
		return maxPerRouatMap;
	}

	public void setMaxPerRouatMap(Map<HttpRoute, Integer> maxPerRouatMap) {
		this.maxPerRouatMap = maxPerRouatMap;
	}

}
