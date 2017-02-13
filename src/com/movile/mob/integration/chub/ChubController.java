package com.movile.mob.integration.chub;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.text.MessageFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.movile.chub.commons.to.CategoryListResponse;
import com.movile.chub.commons.to.CategoryTO;
import com.movile.chub.commons.to.ResourceListResponse;
import com.movile.mob.integration.HttpUtil;

public final class ChubController {

    private static String listCategoriesUrl;

    private static String listResourcesUrl;

    private static String downloadUrl;

    private static String categoryToList;

    private ChubController() {
    }

    public static void init(String listCategoriesUrl, String listResourcesUrl, String downloadUrl) {
        ChubController.listCategoriesUrl = listCategoriesUrl;
        ChubController.listResourcesUrl = listResourcesUrl;
        ChubController.downloadUrl = downloadUrl;
    }

    public static String getCategoryToList() {
        return categoryToList;
    }

    public static void setCategoryToList(String categoryToList) {
        ChubController.categoryToList = categoryToList;
    }

    public static CategoryListResponse getApplicationCategories(Long applicationId) {

        try {

            String url = MessageFormat.format(listCategoriesUrl, String.valueOf(applicationId));
            String response = HttpUtil.doGet(url);
            JAXBContext context = JAXBContext.newInstance(CategoryListResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (CategoryListResponse) unmarshaller.unmarshal(new ByteArrayInputStream(response.getBytes()));

        } catch (Exception e) {
            throw new RuntimeException("[CHUB] Categories load error - Message: " + e.getMessage() + " for " + applicationId);
        }
    }

    public static ResourceListResponse getResourcesOfCategory(CategoryTO category) {

        try {

            String url = MessageFormat.format(listResourcesUrl, String.valueOf(category.getId()));
            String response = HttpUtil.doGet(url);
            JAXBContext context = JAXBContext.newInstance(ResourceListResponse.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ResourceListResponse) unmarshaller.unmarshal(new ByteArrayInputStream(response.getBytes()));

        } catch (Exception e) {
            throw new RuntimeException("[CHUB] Resources load error - Message: " + e.getMessage() + " category " + category);
        }
    }

    // TODO add API into cHub to validate content
    public static boolean existsContent(String resourceId, String userAgent) {
        try {
            return HttpUtil.getHttpCode(MessageFormat.format(downloadUrl, resourceId), userAgent) == 200;
        } catch (Exception e) {
            throw new RuntimeException("[CHUB] Error checking if resource " + resourceId + " exists");
        }
    }

    // TODO add API into cHub to validate content
    public static HttpURLConnection getDownloadConnection(String resourceId, String userAgent) {
        try {
            return HttpUtil.getConnection(MessageFormat.format(downloadUrl, resourceId), userAgent);
        } catch (Exception e) {
            throw new RuntimeException("[CHUB] Error connecting to resource " + resourceId);
        }
    }
}
