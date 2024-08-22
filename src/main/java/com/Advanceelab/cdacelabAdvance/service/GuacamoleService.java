//package com.Advanceelab.cdacelabAdvance.service;
//
//import java.net.URI;
//import java.net.URLEncoder;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpRequest.BodyPublishers;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//@Service
//public class GuacamoleService {
//
//	private final String host = "http://10.10.87.240:8080";
//    private final String username = "admin";
//    private final String password = "cdac@123!@#";
//    
//    private final HttpClient httpClient;
//    private final ObjectMapper objectMapper;
//
//    private final String dataSource = "postgresql";
//    
//    public GuacamoleService() {
//        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build(); //Setting version to HTTP 1.1 is necessary for Guacamole API
//        this.objectMapper = new ObjectMapper();
//    }
//
//    public CompletableFuture<String> getAdminToken() {
//        String formData = String.format("username=%s&password=%s", 
//           URLEncoder.encode(username, StandardCharsets.UTF_8), 
//           URLEncoder.encode(password, StandardCharsets.UTF_8)
//        );
//
//        HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create(host + "/api/tokens"))
//            .header("Content-Type", "application/x-www-form-urlencoded")
//            .POST(BodyPublishers.ofString(formData))
//            .build();
//
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//                if (response.statusCode() == 200) {
//                    String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    return jsonNode.get("authToken").asText();
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                return "Request failed due to an exception";
//            }
//        });
//    }
//    
//    public CompletableFuture<String> getUserToken(String user, String pass) {
//        String formData = String.format("username=%s&password=%s", 
//           URLEncoder.encode(user, StandardCharsets.UTF_8), 
//           URLEncoder.encode(pass, StandardCharsets.UTF_8)
//        );
//
//        HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create(host + "/api/tokens"))
//            .header("Content-Type", "application/x-www-form-urlencoded")
//            .POST(BodyPublishers.ofString(formData))
//            .build();
//
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//                if (response.statusCode() == 200) {
//                    String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    return jsonNode.get("authToken").asText();
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                return "Request failed due to an exception";
//            }
//        });
//    }    
//    
//    public CompletableFuture<Boolean> checkDetailsOfUser(String username, String authToken) {
//    	
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/users/"+username+"?token="+ authToken))
//                .GET()
//                .build();
//    	return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//                if (response.statusCode() == 200) {
//                    String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    return jsonNode.get("username").asText().equals(username);
//                } else {
//    				return false;
//    			}
//            } catch (Exception e) {
//                return false;
//            }
//        });
//    }
//    
//    public CompletableFuture<String> createUser(String username, String password, String authToken, String fullName) {
//       
//    	String formData = "{\r\n"
//    			+ "  \"username\": \""+username+"\",\r\n"
//    			+ "  \"password\": \""+password+"\",\r\n"
//    			+ "  \"attributes\": {\r\n"
//    			+ "    \"disabled\": \"\",\r\n"
//    			+ "    \"expired\": \"\",\r\n"
//    			+ "    \"access-window-start\": \"\",\r\n"
//    			+ "    \"access-window-end\": \"\",\r\n"
//    			+ "    \"valid-from\": \"\",\r\n"
//    			+ "    \"valid-until\": \"\",\r\n"
//    			+ "    \"timezone\": null,\r\n"
//    			+ "    \"guac-full-name\": \""+fullName+"\",\r\n"
//    			+ "    \"guac-organization\": \"\",\r\n"
//    			+ "    \"guac-organizational-role\": \"\"\r\n"
//    			+ "  }\r\n"
//    			+ "}";
//    	
//        HttpRequest request = HttpRequest.newBuilder()
//            .uri(URI.create(host + "/api/session/data/"+dataSource+"/users?token="+authToken))
//            .header("Content-Type", "application/json")
//            .POST(BodyPublishers.ofString(formData))
//            .build();
//
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//                if (response.statusCode() == 200) {
//                	String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    if(jsonNode.get("username").asText().equals(username)) {
//                    	return "User created in guacamole";
//                    } else {
//                    	return null;
//                    }
//                   
//                }
//                else {
//                	String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    if(jsonNode.get("message").asText().contains("already exists")) {
//                    	return "User already exists";
//                    }
//                    else {
//                    	return null;
//                    }
//                }
//            } catch (Exception e) {
//                return null;
//            }
//        });
//    }
//    
//    //public CompletableFuture<String> createConnection(String machineName, String hostname, String machineUsername, String machinePassword, String authToken) {
//    public CompletableFuture<String> createConnection(String machineName, String hostname, String authToken) {
//    	
//    	String formData = "{\r\n"
//    			+ "  \"parentIdentifier\": \"ROOT\",\r\n"
//    			+ "  \"name\": \""+machineName+"\",\r\n"
//    			+ "  \"protocol\": \"rdp\",\r\n"
//    			+ "  \"parameters\": {\r\n"
//    			+ "    \"port\": \"3389\",\r\n"
//    			+ "    \"read-only\": \"\",\r\n"
//    			+ "    \"swap-red-blue\": \"\",\r\n"
//    			+ "    \"cursor\": \"\",\r\n"
//    			+ "    \"color-depth\": \"\",\r\n"
//    			+ "    \"clipboard-encoding\": \"\",\r\n"
//    			+ "    \"disable-copy\": \"\",\r\n"
//    			+ "    \"disable-paste\": \"\",\r\n"
//    			+ "    \"dest-port\": \"\",\r\n"
//    			+ "    \"recording-exclude-output\": \"\",\r\n"
//    			+ "    \"recording-exclude-mouse\": \"\",\r\n"
//    			+ "    \"recording-include-keys\": \"\",\r\n"
//    			+ "    \"create-recording-path\": \"\",\r\n"
//    			+ "    \"enable-sftp\": \"\",\r\n"
//    			+ "    \"sftp-port\": \"\",\r\n"
//    			+ "    \"sftp-server-alive-interval\": \"\",\r\n"
//    			+ "    \"enable-audio\": \"\",\r\n"
//    			+ "    \"security\": \"any\",\r\n"
//    			+ "    \"disable-auth\": \"\",\r\n"
//    			+ "    \"ignore-cert\": \"true\",\r\n"
//    			+ "    \"gateway-port\": \"\",\r\n"
//    			+ "    \"server-layout\": \"\",\r\n"
//    			+ "    \"timezone\": \"\",\r\n"
//    			+ "    \"console\": \"\",\r\n"
//    			+ "    \"width\": \"\",\r\n"
//    			+ "    \"height\": \"\",\r\n"
//    			+ "    \"dpi\": \"\",\r\n"
//    			+ "    \"resize-method\": \"\",\r\n"
//    			+ "    \"console-audio\": \"\",\r\n"
//    			+ "    \"disable-audio\": \"\",\r\n"
//    			+ "    \"enable-audio-input\": \"\",\r\n"
//    			+ "    \"enable-printing\": \"\",\r\n"
//    			+ "    \"enable-drive\": \"\",\r\n"
//    			+ "    \"create-drive-path\": \"\",\r\n"
//    			+ "    \"enable-wallpaper\": \"\",\r\n"
//    			+ "    \"enable-theming\": \"\",\r\n"
//    			+ "    \"enable-font-smoothing\": \"\",\r\n"
//    			+ "    \"enable-full-window-drag\": \"\",\r\n"
//    			+ "    \"enable-desktop-composition\": \"\",\r\n"
//    			+ "    \"enable-menu-animations\": \"\",\r\n"
//    			+ "    \"disable-bitmap-caching\": \"\",\r\n"
//    			+ "    \"disable-offscreen-caching\": \"\",\r\n"
//    			+ "    \"disable-glyph-caching\": \"\",\r\n"
//    			+ "    \"preconnection-id\": \"\",\r\n"
//    			+ "    \"hostname\": \""+hostname+"\",\r\n"
//    			+ "    \"username\": \"\",\r\n"
//    			+ "    \"password\": \"\",\r\n"
////    			+ "    \"username\": \""+machineUsername+"\",\r\n"
////    			+ "    \"password\": \""+machinePassword+"\",\r\n"
//    			+ "    \"domain\": \"\",\r\n"
//    			+ "    \"gateway-hostname\": \"\",\r\n"
//    			+ "    \"gateway-username\": \"\",\r\n"
//    			+ "    \"gateway-password\": \"\",\r\n"
//    			+ "    \"gateway-domain\": \"\",\r\n"
//    			+ "    \"initial-program\": \"\",\r\n"
//    			+ "    \"client-name\": \"\",\r\n"
//    			+ "    \"printer-name\": \"\",\r\n"
//    			+ "    \"drive-name\": \"\",\r\n"
//    			+ "    \"drive-path\": \"\",\r\n"
//    			+ "    \"static-channels\": \"\",\r\n"
//    			+ "    \"remote-app\": \"\",\r\n"
//    			+ "    \"remote-app-dir\": \"\",\r\n"
//    			+ "    \"remote-app-args\": \"\",\r\n"
//    			+ "    \"preconnection-blob\": \"\",\r\n"
//    			+ "    \"load-balance-info\": \"\",\r\n"
//    			+ "    \"recording-path\": \"\",\r\n"
//    			+ "    \"recording-name\": \"\",\r\n"
//    			+ "    \"sftp-hostname\": \"\",\r\n"
//    			+ "    \"sftp-host-key\": \"\",\r\n"
//    			+ "    \"sftp-username\": \"\",\r\n"
//    			+ "    \"sftp-password\": \"\",\r\n"
//    			+ "    \"sftp-private-key\": \"\",\r\n"
//    			+ "    \"sftp-passphrase\": \"\",\r\n"
//    			+ "    \"sftp-root-directory\": \"\",\r\n"
//    			+ "    \"sftp-directory\": \"\"\r\n"
//    			+ "  },\r\n"
//    			+ "  \"attributes\": {\r\n"
//    			+ "    \"max-connections\": \"\",\r\n"
//    			+ "    \"max-connections-per-user\": \"\",\r\n"
//    			+ "    \"weight\": \"\",\r\n"
//    			+ "    \"failover-only\": \"\",\r\n"
//    			+ "    \"guacd-port\": \"\",\r\n"
//    			+ "    \"guacd-encryption\": \"\",\r\n"
//    			+ "    \"guacd-hostname\": \"\"\r\n"
//    			+ "  }\r\n"
//    			+ "}";
//    	
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/connections?token="+authToken))
//                .header("Content-Type", "application/json")
//                .POST(BodyPublishers.ofString(formData))
//                .build();
//    	
//    	return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//                if (response.statusCode() == 200) {
//                	String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    if(jsonNode.get("name").asText().equals(machineName)) {
//                    	return "RDP connection created in guacamole.";
//                    } else {
//                    	return null;
//                    }
//                   
//                }
//                else {
//                	String responseBody = response.body();
//                    JsonNode jsonNode = objectMapper.readTree(responseBody);
//                    if(jsonNode.get("message").asText().contains("already exists")) {
//                    	return "RDP connection already exists.";
//                    }
//                    else {
//                    	return null;
//                    }
//                }
//            } catch (Exception e) {
//                return null;
//            }
//        });
//    }
//    
//    public CompletableFuture<Boolean> assignConnection(String username, String authToken, String connectionIdentifier) {
//    	
//    	String formData = "[\r\n"
//    			+ "  {\r\n"
//    			+ "    \"op\": \"add\",\r\n"
//    			+ "    \"path\": \"/connectionPermissions/"+connectionIdentifier+"\",\r\n"
//    			+ "    \"value\": \"READ\"\r\n"
//    			+ "  }\r\n"
//    			+ "]";
//    	
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/users/"+username+"/permissions?token="+authToken))
//                .header("Content-Type", "application/json")
//                .method("PATCH",BodyPublishers.ofString(formData))
//                .build();
//    	
//    	return CompletableFuture.supplyAsync(() -> {
//            try {
//                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//                if (response.statusCode() == 204) {
//                    return true;
//	            } else {
//                	return false;
//                }
//            } catch (Exception e) {
//                return false;
//            }
//        });
//    }
//    
//    public CompletableFuture<String> getConnectionIdentifier(String machineName, String authToken) {
//    	
//    	//This api returns the list of all connections
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/connections?token="+authToken))
//                .GET()
//                .build();
//    	
//    	return CompletableFuture.supplyAsync(() -> {
//    		try {
//	    		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//	    		if(response.statusCode() == 200) {
//	    			String responseBody = response.body();
//	    			JsonNode jsonData = objectMapper.readTree(responseBody);
//	    			if(jsonData != null) {
//	    				for(JsonNode jsonNode : jsonData) {
//	    					if(jsonNode.get("name").asText().equals(machineName)) {
//	    						return jsonNode.get("identifier").asText();
//	    					}
//	    				}
//	    				return null;
//	    			}
//	    			else {
//	    				return null;
//	    			}
//	    		} else {
//	    			return null;
//	    		}
//			 } catch (Exception e) {
//	             return null;
//	         }
//    	});
//    }
//    
////  public String getVMUrl(String identifier, String userToken) {
////	
////	String connectionId = identifier+"\0c\0"+dataSource;
////	connectionId = Base64.getEncoder().encodeToString(connectionId.getBytes());
////	
////	String url = host+"/#/client/"+connectionId+"?token="+userToken;
////	return url;
////}
//
//	public String getVMUrl(String vmName) throws InterruptedException, ExecutionException {
//	
//		String authToken = getAdminToken().get();
//		String connectionIdentifier = getConnectionIdentifier(vmName, authToken).get();
//		String connectionId = connectionIdentifier+"\0c\0"+dataSource;
//		connectionId = Base64.getEncoder().encodeToString(connectionId.getBytes());
//		
//		String url = host+"/#/client/"+connectionId;
//		return url;
//	}
//    
//    public CompletableFuture<Boolean> deleteConnection(String identifier, String authToken) {
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/connections/"+identifier+"?token="+authToken))
//                .DELETE()
//                .build();
//    	return CompletableFuture.supplyAsync(() -> {
//    		try {
//	    		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//	    		if(response.statusCode() == 204) {
//	    			return true;
//	    		} else {
//    				return false;
//    			}
//			 } catch (Exception e) {
//	             return false;
//	         }
//    	});
//    }
//    
//    public CompletableFuture<Boolean> deleteUser(String username, String authToken) {
//    	HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(host + "/api/session/data/"+dataSource+"/users/"+username+"?token="+authToken))
//                .DELETE()
//                .build();
//    	return CompletableFuture.supplyAsync(() -> {
//    		try {
//	    		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//	    		if(response.statusCode() == 204) {
//	    			return true;
//	    		} else {
//    				return false;
//    			}
//			 } catch (Exception e) {
//	             return false;
//	         }
//    	});
//    }
//    
//}
