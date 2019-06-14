package rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.PuSelector;
import utils.SwappiData;

/**
 * @author lam@cphbusiness.dk
 */
@Path("exam")
public class DemoResource {

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {
        EntityManager em = PuSelector.getEntityManagerFactory("pu").createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("apis")
//    public String getFromAPIs() {
//        return swappiFutureCalls();
//    }
//
//    public static String swappiFutureCalls() {
//        ForkJoinPool executor = new ForkJoinPool(25,
//                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
//                null, false);
//        List<Future<String>> futureArrayList = new ArrayList();
//        for (int i = 1; i < 6; i++) {
//            Callable<String> worker = new SwappiData(i);
//            futureArrayList.add(executor.submit(worker));
//        }
//        List<String> res = new ArrayList();
//        futureArrayList.parallelStream().forEach(future -> {
//            try {
//                String getFutureStr = future.get(5, TimeUnit.SECONDS);
//                res.add(getFutureStr);
//            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
//
//            }
//        });
//        return res.toString();
//    }
}
