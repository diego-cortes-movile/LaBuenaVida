package com.movile.labuenavida.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movile.labuenavida.util.ConfigurationProvider;
import com.movile.labuenavida.util.MovileSdkHolder;
import com.movile.sdk.exceptions.ProcessOperationException;
import com.movile.sdk.services.chub.model.Category;
import com.movile.sdk.services.chub.model.CategoryListResponse;
import com.movile.sdk.services.chub.model.Resource;
import com.movile.sdk.services.chub.model.ResourceListRequest;
import com.movile.sdk.services.chub.model.ResourceListResponse;

/**
 * Operations related to cHub.
 * 
 * @author Yvan Lopez (yvan.lopez@movile.com)
 *
 */
public class ChubBO {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    private static final ChubBO instance = new ChubBO();

    public static ChubBO getInstace() {
        return instance;
    }

    /**
     * Obtiene la lista de todos los videos de todas las categorias.
     * 
     * @return Lista de videos.
     */
    public List<Resource> getAllVideosFromAllCategories() {

        List<Category> categories = new ArrayList<>();
        List<Resource> videos = new ArrayList<>();

        String chubAppId = ConfigurationProvider.getApplicationProperties().getString("chub.application.id");

        if (NumberUtils.isDigits(chubAppId)) {
            categories = getAllCategoriesByChubAppId(Long.valueOf(chubAppId));
            SYSTEM_LOGGER.info("Categorias obtenidas: {}", categories);

            videos = getAllVideosFromCategories(categories, chubAppId);
            SYSTEM_LOGGER.info("Videos obtenidos: {}", categories);
        }

        return videos;
    }

    /**
     * Obtiene todos los videos de todas las categorias.
     * 
     * @param categories Las categorias a recorrer.
     * @param cHubAppId cHub application id.
     * @return Lista de videos.
     */
    private List<Resource> getAllVideosFromCategories(List<Category> categories, String cHubAppId) {

        List<Resource> videos = new ArrayList<>();

        for (Category category : categories) {

            // Esta categoria se hizo con fines de prueba. No se iterará.
            if (category.getId() == 2301L) {
                continue;
            }

            ResourceListRequest req = new ResourceListRequest();
            req.setApplicationId(Integer.parseInt(cHubAppId));
            req.setCategoryId(String.valueOf(category.getId()));

            ResourceListResponse rspns = MovileSdkHolder.getcHubClient().getResources(req);

            if (rspns != null && CollectionUtils.isNotEmpty(rspns.getResources())) {
                for (Resource video : rspns.getResources()) {
                    videos.add(video);
                }
            }

        }

        return videos;
    }

    /**
     * Obtiene una lista de categorias por ChubAppId.
     * 
     * @param chubAppId Application ID del cHub.
     * @return Lista de categorias.
     */
    public List<Category> getAllCategoriesByChubAppId(Long chubAppId) {
        CategoryListResponse rspns = null;
        try {
            rspns = MovileSdkHolder.getcHubClient().listCategory(Long.valueOf(chubAppId));
        } catch (NumberFormatException | ProcessOperationException e) {
            EXCEPTION_LOGGER.error("Formato de numero incorreco o mal proceso de operacion: {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            EXCEPTION_LOGGER.error("Error en el sistema: {}", e.getMessage());
            e.printStackTrace();
        }

        if (rspns != null) {
            return rspns.getCategories();
        }

        return null;
    }

}
