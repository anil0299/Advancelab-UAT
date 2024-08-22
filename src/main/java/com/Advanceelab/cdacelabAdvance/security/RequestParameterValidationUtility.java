package com.Advanceelab.cdacelabAdvance.security;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestParameterValidationUtility {

	private static final Log logger = LogFactory
			.getLog(RequestParameterValidationUtility.class);

	public static boolean checkRequest(HttpServletRequest request, String url,
			int numParams, String[] paramsArr) {

		if (!RequestParameterValidationUtility.checkURI(url,
				request.getRequestURI())) {
			return false;
		}
		if (request.getParameterMap().containsKey("_csrf")) {
			numParams++;
		}
		if (request.getParameterMap().containsKey("hppCode")) {
			numParams++;
		}
		if (request.getParameterMap().containsKey("randkey")) {
			numParams++;
		}
		if (request.getParameterMap().size() > numParams) {
			System.out.println("Incorrect Number of Parameters: Expected "
					+ numParams + ", Received "
					+ request.getParameterMap().size() + " For URL " + url);

			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				System.out.println(paramName);
			}
			return false;
		}
		if (paramsArr != null
				&& !checkRequestParams(request.getParameterNames(), paramsArr)) {
			return false;
		}
		return true;
	}

	public static boolean checkExamRequest(HttpServletRequest request,
			String url, int numParams, String[] paramsArr) {

		if (!checkExamURI(url, request.getRequestURI())) {
			return false;
		}
		if (request.getParameterMap().containsKey("_csrf")) {
			numParams++;
		}
		if (request.getParameterMap().containsKey("hppCode")) {
			numParams++;
		}
		if (request.getParameterMap().containsKey("randkey")) {
			numParams++;
		}
		if (request.getParameterMap().size() > numParams) {
			System.out.println("Incorrect Number of Parameters: Expected "
					+ numParams + ", Received "
					+ request.getParameterMap().size());
			return false;
		}
		if (paramsArr != null
				&& !checkRequestParams(request.getParameterNames(), paramsArr)) {
			return false;
		}
		return true;
	}

	private static boolean checkRequestParams(
			Enumeration<String> parameterNames, String[] paramsArr) {

		List<String> params = Arrays.asList(paramsArr);

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();

			if (paramName.equals("_csrf")) {
				continue;
			}
			if (paramName.equals("hppCode")) {
				continue;
			}
			if (paramName.equals("randkey")) {
				continue;
			}
			if (!params.contains(paramName)) {
				System.out.println("Received Un-expected Parameter '"
						+ paramName + "'");
				return false;
			}
		}
		return true;
	}

	public static boolean checkURI(String handlerURL, String requestURI) {

		System.out.println("handlerURL--->" + handlerURL);
		System.out.println("requestURI--->" + requestURI);

		String[] uriParts = requestURI.split("/");

		if (handlerURL.equals(uriParts[2])) {
			return true;
		} else {
			System.out.println("Request URL is '" + uriParts[1]
					+ "' instead of '" + handlerURL + "'.");
			return false;
		}
	}

	public static boolean checkExamURI(String handlerURL, String requestURI) {

		String[] uriParts = requestURI.split("/");

		if (handlerURL.equals(uriParts[3])) {
			return true;
		} else {
			System.out.println("Request URL is '" + uriParts[3]
					+ "' instead of '" + handlerURL + "'.");
			return false;
		}
	}

	public static boolean countryId(HttpServletRequest request) {

		if (request.getMethod().equalsIgnoreCase(Constants.POST)
				&& request.getQueryString() != null) {
			logger.error("Query Parameters in Post Request: "
					+ request.getRequestURI());
			logger.error("Query Parameters: " + request.getQueryString());
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateRequestForFileHPP(String[] paramValues,
			String hppCodeInRequest) {

		StringBuilder inputCode = new StringBuilder();
		for (String paramValue : paramValues) {
			inputCode.append(EncodingUtility.getLongInputCode(paramValue));
		}
		String hppCode = EncodingUtility.getHPPCode(Constants.ORIG_HPP_CODE,
				inputCode);
		 System.out.println("Calculated HPP Code: " + hppCode);
		 System.out.println("HPP Code in Request: " + hppCodeInRequest);

		if (!hppCode.equals(hppCodeInRequest)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean validateRequestForHPP(String[] paramValues,
			String hppCodeInRequest) {

		StringBuilder inputCode = new StringBuilder();
		System.out.println("double code ");
		for (String paramValue : paramValues) {
		    inputCode.append(EncodingUtility.getInputCode(paramValue));
		}

//		inputCode.append(EncodingUtility.getInputCode(inputCode + ""));
		System.out.println("input code: "+inputCode);
		String hppCode = EncodingUtility.getHPPCode(Constants.ORIG_HPP_CODE,
				inputCode);
		System.out.println("hppCode1234: "+hppCode);
		if (!hppCode.equals(hppCodeInRequest)) {
			System.out.println("-------------------------------------");
			System.out.println("ERROR: HTTP Parameter Pollution");
			System.out.println("Calculated HPP Code: " + hppCode);
			System.out.println("HPP Code in Request: " + hppCodeInRequest);
			System.out.println("-------------------------------------");
			inputCode = new StringBuilder();
			for (String paramValue : paramValues) {
				inputCode.append(EncodingUtility.getInputCode(paramValue));
				System.out.println("Param Value: " + paramValue
						+ ", Input Code: " + inputCode);
			}
			return false;
		} else {
			return true;
		}
	}

	public static boolean validateRequestForHPP(String[] paramValues,
			String[] paramValuesWithLines, String hppCodeInRequest) {

		StringBuilder inputCode = new StringBuilder();

		for (String paramValue : paramValues) {
		    inputCode.append(EncodingUtility.getInputCode(paramValue));
		}
		for (String paramValue : paramValuesWithLines) {
			inputCode.append(EncodingUtility.getInputCodeWithLines(paramValue));
		}
		String hppCode = EncodingUtility.getHPPCode(Constants.ORIG_HPP_CODE,
				inputCode);

		if (!hppCode.equals(hppCodeInRequest)) {
			System.out.println("-------------------------------------");
			System.out.println("ERROR: HTTP Parameter Pollution");
			System.out.println("Calculated HPP Code: " + hppCode);
			System.out.println("HPP Code in Request: " + hppCodeInRequest);
			System.out.println("-------------------------------------");

			inputCode = new StringBuilder();

			for (String paramValue : paramValues) {
				inputCode.append(EncodingUtility.getInputCode(paramValue));
				System.out.println("Param Value: " + paramValue
						+ ", Input Code: " + inputCode);
			}
			for (String paramValue : paramValuesWithLines) {
				inputCode.append(EncodingUtility.getInputCodeWithLines(paramValue));
				System.out.println("Param Value: " + paramValue
						+ ", Input Code: " + inputCode);
			}
			return false;
		} else {
			return true;
		}
	}

	public static String checkRequest(HttpServletRequest request,
												 String[] params, String hppCode) {

		// Check query parameters in POST request
		// --------------------------------------
		if (RequestParameterValidationUtility
				.checkQueryParamsInPostRequest(request)) {
			return "incorrect";
		}
		// HTTP Parameter Pollution
		// --------------------------------------
		if (!RequestParameterValidationUtility.validateRequestForHPP(params,
				hppCode)) {
			return "incorrect";
		}
		return null;
	}
//
	public static boolean checkQueryParamsInPostRequest(
			HttpServletRequest request) {

		if (request.getMethod().equalsIgnoreCase(Constants.POST)
				&& request.getQueryString() != null) {
			logger.error("Query Parameters in Post Request: "
					+ request.getRequestURI());
			logger.error("Query Parameters: " + request.getQueryString());
			return true;
		} else {
			return false;
		}
	}

	public static String encodeData(String input) {

		StringBuffer output = new StringBuffer();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			int ascii = (int) c;

			if (ascii >= 65 && ascii <= 90) {
				output.append((char) ((c - 65 + 3) % 26 + 65)); // Uppercase
			}

			else if (c >= 48 && c <= 56) {
				output.append((char) ((c - 48 + 3) % 26 + 48)); // for
																// Number(0-9)
			}

			else if (ascii >= 97 && ascii <= 122) {
				output.append((char) ((c - 97 + 3) % 26 + 97)); // Lowercase

			} else {
				output.append(c);
			}
		}
		return output.toString();
	}
}
