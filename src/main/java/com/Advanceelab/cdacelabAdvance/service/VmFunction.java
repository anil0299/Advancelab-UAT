package com.Advanceelab.cdacelabAdvance.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class VmFunction {

	private Logger logger = Logger.getLogger(getClass().getName());
	
    private final HttpClient httpClient;

    public VmFunction() {
    	try {
            // Initialize FileHandler for logging
            FileHandler fileHandler = new FileHandler("src/main/resources/static/logs/log.txt",true);
            logger.addHandler(fileHandler);
        } catch (IOException ex) {
            System.out.println("Log handler error: " + ex.getMessage());
        }
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompletableFuture<String> searchUuid(String string, String auth) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/list"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic " + auth)
                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"kind\": \"vm\",\n  \"offset\": 0,\n  \"length\": 123,\n  \"filter\": \"vm_name==" + string + "\",\n  \"sort_order\": \"ASCENDING\",\n  \"sort_attribute\": \"ASCENDING\"\n}"))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                String responseBody = response.body();
                boolean entitiesExists = Integer.parseInt(JsonPath.read(responseBody,"$.entities.length()").toString())>0;
                if(entitiesExists) {
                	return JsonPath.read(responseBody, "$.entities[0].metadata.uuid");
                } else {
                	return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<String> searchIp(String string, String auth) {
        return CompletableFuture.supplyAsync(() -> {
	            try {
	                HttpRequest request = HttpRequest.newBuilder()
	                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/list"))
	                        .header("Content-Type", "application/json")
	                        .header("Authorization", "Basic " + auth)
	                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"kind\": \"vm\",\n  \"offset\": 0,\n  \"length\": 123,\n  \"filter\": \"vm_name==" + string + "\",\n  \"sort_order\": \"ASCENDING\",\n  \"sort_attribute\": \"ASCENDING\"\n}"))
	                        .build();
	                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	                String responseBody = response.body();
	                String ip = JsonPath.read(responseBody, "$.entities[0].status.resources.nic_list[0].ip_endpoint_list.[0].ip");
	                logger.log(Level.INFO, "Ip found: " + ip);
	                return ip;
	            } catch (Exception ex) {
	            	logger.log(Level.INFO, "Ip not found");
                    return null;
	            }
        });
    }

    public CompletableFuture<String> searchVmName(String string, String auth) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/list"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic " + auth)
                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"kind\": \"vm\",\n  \"offset\": 0,\n  \"length\": 123,\n  \"filter\": \"vm_name==" + string + "\",\n  \"sort_order\": \"ASCENDING\",\n  \"sort_attribute\": \"ASCENDING\"\n}"))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                if (jsonNode.has("entities") && jsonNode.get("entities").isArray() && jsonNode.get("entities").size() > 0) {
                    return JsonPath.read(responseBody, "$.entities[0].status.name");
                } else {
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Void> powerOn(String string, String auth) {
        return CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v0.8/vms/set_power_state/fanout"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46Q2RhY0AxMjMhQCM=")
                        .method("POST", HttpRequest.BodyPublishers.ofString("[{\"generic_dto\":{\"transition\":\"on\",\"uuid\":\"" + string + "\"},\"cluster_uuid\":\"0005a833-66d0-008c-62e3-08f1ea7d7714\"}]"))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> deleteVm(String vmUuid) {
        return CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/" + vmUuid))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic YWRtaW46Q2RhY0AxMjMhQCM=")
                        .method("DELETE", HttpRequest.BodyPublishers.noBody())
                        .build();
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                //HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                //System.out.println(response.body());
                //System.out.println("Deleted Vm with uuid " + vmUuid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> checkVmBasic(String stringwin, String stringlinux, String auth) {
        CompletableFuture<String> winNameFuture = searchVmName(stringwin, auth);
        CompletableFuture<String> linuxNameFuture = searchVmName(stringlinux, auth);
        return winNameFuture.thenCombineAsync(linuxNameFuture, (winName, linuxName) -> {
            if (winName != null && linuxName != null) {
                System.out.println("1");
                return true;
            } else {
                return false;
            }
        });
    }

    public CompletableFuture<Void> cloneVm(String uuid, String auth, String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/" + uuid + "/clone"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic " + auth)
                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"override_spec\": {\n    \"name\": \"" + name + "\"\n   }\n   }"))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> cloneVmWindow(String string, String auth) {
        return cloneVm("e5c3906d-2193-49a7-8001-7ae212b09cfc", auth, string); // 250e0f8b-1efc-4cb6-b614-782d4ff7cffd
    }

    public CompletableFuture<Void> cloneVmLinux(String string, String auth) {
        return cloneVm("078c4d90-6eff-479a-ad43-aa385d704c10", auth, string); // ff3d34bf-240c-4e89-be35-b6d36cdffc16
    }

    public CompletableFuture<Integer> checkVm(String vmName, String auth) {
        return searchVmName(vmName, auth)
                .thenApplyAsync(name -> name != null ? 1 : -1);
    }
}

