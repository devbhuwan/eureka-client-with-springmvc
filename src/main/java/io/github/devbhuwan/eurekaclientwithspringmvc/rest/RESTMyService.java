package io.github.devbhuwan.eurekaclientwithspringmvc.rest;

import com.netflix.appinfo.*;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static io.github.devbhuwan.eurekaclientwithspringmvc.rest.RESTMyService.SERVICE_ROOT_URL;

/**
 * @author Bhuwan Prasad Upadhyay
 * @date 04/02/2017
 */
@Path(SERVICE_ROOT_URL)
public class RESTMyService {

    public static final String ROOT_PATH = "/eureka-client/rest";
    public static final String SERVICE_ROOT_URL = "/myservice";
    private final String serviceIp;
    private final String serviceAppGroup;
    private final String serviceHostname;
    private final int servicePort;

    public RESTMyService() {
        this.serviceIp = "127.0.0.1";
        this.serviceHostname = "localhost";
        this.serviceAppGroup = "SRV-GRP";
        this.servicePort = 8080;
        this.initEurekaConfiguration();

        /* you can register multiple services using registerService(serviceName,serviceUrl);*/
        this.registerService("my-service-by-text", "/{text}");
        this.registerService("hello-world-by-text", "/hello-world/{text}");
    }

    private void initEurekaConfiguration() {
        ConfigurationManager.getConfigInstance()
                .setProperty("eureka.region", "default");
        ConfigurationManager.getConfigInstance()
                .setProperty("eureka.serviceUrl.default", "http://localhost:8080/eureka/v2/");
    }

    /* service - 1 */
    @GET
    @Path("/{text}")
    public Response getMsg(@PathParam("text") String msg) {
        return Response.status(200)
                .entity("Your parameter is {" + msg + "}")
                .build();
    }

    /* service - 2 */
    @GET
    @Path("/hello-world/{text}")
    public Response helloWorldText(@PathParam("text") String msg) {
        return Response.status(200)
                .entity("Hello World {" + msg + "}")
                .build();
    }


    @GET
    @Path("/healthCheck")
    public Response healthCheck() {
        return Response.status(200)
                .entity("alive")
                .build();
    }

    @GET
    @Path("/status")
    public Response status() {
        return Response.status(200)
                .build();
    }

    private void registerService(String serviceName, String serviceUrl) {
        EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig(serviceName);
        InstanceInfo instanceInfo = buildInstanceInfo(serviceName, serviceUrl);
        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, new DefaultEurekaClientConfig());
        eurekaClient.registerHealthCheck(instanceStatus -> {
            /* write code to check health of service and make change status of service in eureka */
            return InstanceInfo.InstanceStatus.UP;
        });
    }

    private InstanceInfo buildInstanceInfo(String serviceName, String serviceUrl) {
        return InstanceInfo.Builder.newBuilder()
                .setAppName(serviceName) /*SERVICE-NAME*/
                .setIPAddr(serviceIp) /*SERVICE HOST IP*/
                .setAppGroupName(serviceAppGroup)
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setHostName(serviceHostname) /*SERVICE HOST NAME*/
                .setInstanceId(serviceName + "_server1")
                .setPort(servicePort) /*SERVICE HOST SERVER PORT*/
                .setVIPAddress(serviceName)
                .setHealthCheckUrls(ROOT_PATH + SERVICE_ROOT_URL + "/healthCheck", null, null) /*SERVICE HEALTH CHECK URL*/
                .setHomePageUrl(ROOT_PATH + SERVICE_ROOT_URL + serviceUrl, null)
                .setStatusPageUrl(ROOT_PATH + SERVICE_ROOT_URL + "/status", null)
                .setLeaseInfo(LeaseInfo.Builder.newBuilder().setDurationInSecs(10).build())
                .build();
    }

}
