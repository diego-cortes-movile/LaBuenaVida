package com.movile.mob.portal.channel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.compera.portal.channel.Channel;
import com.compera.portal.channel.ChannelElement;
import com.compera.portal.channel.ChannelStorage;
import com.compera.portal.log.Logger;
import com.compera.portal.parser.bean.Item;
import com.compera.portal.process.Process;
import com.compera.portal.process.ProcessException;
import com.compera.portal.property.ArrayProperty;
import com.compera.portal.property.SimpleProperty;
import com.movile.chub.commons.to.CategoryListResponse;
import com.movile.chub.commons.to.CategoryTO;
import com.movile.chub.commons.to.ResourceListResponse;
import com.movile.chub.commons.to.ResourceTO;
import com.movile.mob.integration.chub.ChubController;
import com.movile.mob.portal.PortalUtil;

public class ChannelLoaderProcess extends Process {

    public enum ContentType {
        Undefined,
        TrueTones,
        Polifonicos,
        Temas,
        PersonalizadasAnimadas,
        Personalizadas,
        SuperResource,
        Animadas,
        Monofonicos,
        Wallpaper,
        Videos,
        Icones,
        CrazyTones,
        Jogos,
        Alertas,
        Android
    }

    public static final String INTERVAL_ATTRIBUTE = "interval";

    public static final String CHANNEL_TYPE_PROPERTY = "channelType";

    public static final String ELEMENT_RESOURCE_TYPE_PROPERTY = "resourceType";

    public static final String ELEMENT_IDENTIFICATION_PROPERTY = "id";

    public static final String ELEMENT_NAME_PROPERTY = "name";

    public static final String ELEMENT_TYPE_PROPERTY = "type";

    public static final String ELEMENT_CONTENT_TYPE_PROPERTY = "contentType";

    public static final String ELEMENT_DOWNLOAD_URL_PROPERTY = "downloadUrl";

    public static final String ELEMENT_PRIZE_PROPERTY = "prize";

    public static final String ELEMENT_CHARGEUNIT_PROPERTY = "chargeUnit";

    public static final String ELEMENT_ADULT_PROPERTY = "adult";

    public static final String ELEMENT_CREDIT_PROPERTY = "credit";

    public static final String ELEMENT_RESOURCE_DESCRIPTION_PROPERTY = "description";

    public static final String ELEMENT_RESOURCE_TYPE_ID_PROPERTY = "contentTypeId";

    public static final String ELEMENT_URL_PREVIEW_PROPERTY = "urlPreview";

    public static final String ELEMENT_URL_DOWNLOAD_PROPERTY = "urlDownload";

    private static final String SLASH = "/";

    private static final String ELEMENT_PREFIX_PROPERTY = "content_";

    private Long interval = null;

    private boolean stop = false;

    private String[] applicationIds;

    private void sleep() {
        final long time = (this.interval == null ? 43200000L : this.interval);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Logger.log("ultramobPortalCore.general", e.getMessage());
        }
    }

    @Override
    protected void initiateProcess(Item item) throws ProcessException {
        try {

            this.applicationIds = PortalUtil.getAttributeArrayValue(item, "application_ids");
            String listCategoriesUrl = PortalUtil.getAttributeSimpleValue(item, "chub_list_category_url");
            String listResourcesUrl = PortalUtil.getAttributeSimpleValue(item, "chub_list_resources_url");
            String downloadUrl = PortalUtil.getAttributeSimpleValue(item, "chub_download_url");

            ChubController.init(listCategoriesUrl, listResourcesUrl, downloadUrl);

        } catch (Exception e) {
            Logger.log("channel.general", e.getMessage());
        }
    }

    @Override
    protected void runProcess() {

        while (!this.stop) {

            try {
                /*
                int length = this.smartchannel_channelIds.length;
                
                if (this.smartchannel_modelIds.length != length)
                    throw new Exception("Error loading smart Channels - Malformed matching model and channel Ids");
                for (int j = 0; j < length; j++) {
                    loadSmartChannels(this.smartchannel_channelIds[j], this.smartchannel_modelIds[j]);
                }
                */
                for (String applicationId : this.applicationIds) {
                    loadChannels(applicationId);
                }
            } catch (Exception e) {
                Logger.log("channel.general", "[SupermobChannelLoader] - Fail on load channels. Message :" + e.getMessage() + ". " + this.interval
                        + "ms to next load.");
            }
            sleep();
        }
    }

    protected void loadChannels(String applicationId) {

        Logger.log("channel.general", "[ChannelLoader] - Loading Channels for : " + applicationId);

        try {

            ChannelStorage channelStorage = ChannelStorage.getInstance();

            CategoryListResponse categories = ChubController.getApplicationCategories(Long.valueOf(applicationId));

            if (categories != null && categories.getCategories() != null && categories.getCategories().size() > 0) {

                Channel channel = null;

                for (CategoryTO category : categories.getCategories()) {

                    ArrayProperty array = null;
                    ResourceListResponse ResourceListResponse = ChubController.getResourcesOfCategory(category);

                    if (ResourceListResponse != null && ResourceListResponse.getResources() != null && ResourceListResponse.getResources().size() > 0) {

                        channel = new Channel(category.getName());
                        array = new ArrayProperty(CHANNEL_TYPE_PROPERTY);
                        channel.addProperty(array);
                        channelStorage.add(channel);
                        Logger.log(Logger.STANDARD_LOG, "Loading channel " + channel.getName());

                        ContentType contentType = null;
                        for (ResourceTO resource : ResourceListResponse.getResources()) {

                            Logger.log(Logger.STANDARD_LOG, "Loaded resource '" + resource.getName() + "' on folder '" + category.getName() + "'");
                            ChannelElement channelElement = new ChannelElement("content_" + resource.getId(), channel);
                            channelElement.addProperty(new SimpleProperty(ELEMENT_IDENTIFICATION_PROPERTY, String.valueOf(resource.getId())));
                            channelElement.addProperty(new SimpleProperty(ELEMENT_NAME_PROPERTY, resource.getName()));
                            channelElement.addProperty(new SimpleProperty(ELEMENT_ADULT_PROPERTY, String.valueOf(resource.getAdult())));
                            channelElement.addProperty(new SimpleProperty(ELEMENT_CONTENT_TYPE_PROPERTY, getResourceType(resource.getResourceType())));
                            channelElement.addProperty(new SimpleProperty(ELEMENT_RESOURCE_DESCRIPTION_PROPERTY, resource.getDescription()));

                            String previewImageUrl = null;

                            if (CollectionUtils.isNotEmpty(resource.getImagePreviewURL())) {
                                previewImageUrl = resource.getImagePreviewURL().iterator().next();
                            }

                            // If the Preview Image is not available, an image provided by the application will be placed.
                            if (StringUtils.isBlank(previewImageUrl) || (previewImageUrl != null && previewImageUrl.endsWith(SLASH))) {
                                String resourceType = resource.getResourceType();

                                // 'TRUETONE' or 'TRUETONES' are valid types.
                                if (resourceType.toUpperCase().contains("TRUETONE")) {
                                    previewImageUrl = "previewImageUrlTruetones";
                                } else if ("VIDEO".equals(resourceType.toUpperCase())) {
                                    previewImageUrl = "previewImageUrlVideo";
                                } else if ("JAVA".equals(resourceType.toUpperCase())) {
                                    previewImageUrl = "previewImageUrlJava";
                                } else {
                                    previewImageUrl = "previewImageUrlDefault";
                                }

                                channelElement.addProperty(new SimpleProperty(ELEMENT_URL_PREVIEW_PROPERTY, previewImageUrl));

                                Logger.log("[ChannelLoader] - Preview Image not found for Resource '" + resource.getName() + "' and Type '" + resourceType
                                        + "'. Image '" + previewImageUrl + "' will be used in its place.");
                            } else {
                                // The Preview Image is available, the resource's value will be placed.
                                channelElement.addProperty(new SimpleProperty(ELEMENT_URL_PREVIEW_PROPERTY, previewImageUrl));
                            }

                            if (CollectionUtils.isNotEmpty(resource.getResourcePreviewURLs())) {

                                channelElement.addProperty(new SimpleProperty("resourcePreviewUrl", resource.getResourcePreviewURLs().iterator().next()));

                            }

                            /*
                             * channelElement.addProperty(new SimpleProperty(ELEMENT_URL_PREVIEW_PROPERTY, resource.getImagePreviewURL().iterator().next()));
                             * channelElement.addProperty(new SimpleProperty(ELEMENT_CREDIT_PROPERTY, resource.getCredit()));
                             * String downloadURL = ContentDownloaderProcessor.buildDownloadURL(Long.valueOf(applicationId), channel.getKey(),
                             * resource.getId());
                             * channelElement.addProperty(new SimpleProperty(ELEMENT_URL_DOWNLOAD_PROPERTY, downloadURL));
                             */

                            if (contentType == null) {
                                channel.addProperty(new SimpleProperty(CHANNEL_TYPE_PROPERTY, getResourceType(resource.getResourceType())));
                            }
                            channel.addElement(channelElement);
                        }
                        
                        Collections.sort(channel.getElements(), ComparatorResource.getInstance());
                        Collections.reverse(channel.getElements());
                    }
                }
            }

        } catch (Exception e) {
            Logger.log(Logger.STANDARD_LOG, "Error on load channels : " + e.getMessage());
        }

        Logger.log("channel.general", "[ChannelLoader] - Channels Ready for : " + applicationId);
    }

    private String getResourceType(String resourceType) {

        ContentType channelType = ContentType.Undefined;

        if (resourceType != null) {

            if (resourceType.equalsIgnoreCase("WALLPAPER")) {
                channelType = ContentType.Wallpaper;
            }

            if (resourceType.equalsIgnoreCase("SCREENSAVER")) {
                channelType = ContentType.Animadas;
            }

            if (resourceType.equalsIgnoreCase("POLYPHONIC")) {
                channelType = ContentType.Polifonicos;
            }

            if (resourceType.equalsIgnoreCase("TRUETONE")) {
                channelType = ContentType.TrueTones;
            }

            if (resourceType.equalsIgnoreCase("VIDEO")) {
                channelType = ContentType.Videos;
            }

            if (resourceType.equalsIgnoreCase("JAVA")) {
                channelType = ContentType.Jogos;
            }

            if (resourceType.equalsIgnoreCase("ANDROID")) {
                channelType = ContentType.Android;
            }
        }
        return channelType.name();
    }

    @Override
    public void terminateProcess() {
    }

    public boolean isStop() {
        return this.stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    
    public static ChannelElement getChannelElementByName(String channelName, String elementId){
        ChannelElement channelElement = null;
        
        ChannelStorage channelStorage = ChannelStorage.getInstance();
        
        Channel channel = channelStorage.get(channelName);
        
        if (channel != null && !channel.getElements().isEmpty()){
                channelElement = channel.getElement(ELEMENT_PREFIX_PROPERTY + elementId);
        }
        
        return channelElement;
    }
    
    public static List<ChannelElement> getChannelElements(String channelName){
        List<ChannelElement> elements = null;
        
        ChannelStorage channelStorage = ChannelStorage.getInstance();
        
        Channel channel = channelStorage.get(channelName);
        
        if (channel == null) {
            return null;
        }
        
        elements = channel.getElements();
        
        return elements;
    }
    
    public static class ComparatorResource implements Comparator<ChannelElement> {

        private static ComparatorResource instance;
        
        @Override
        public int compare(ChannelElement arg0, ChannelElement arg1) {
            return  Integer.parseInt(arg0.getProperty("id").getSimpleValue()) - Integer.parseInt(arg1.getProperty("id").getSimpleValue());
        }
        
        public static ComparatorResource getInstance(){
            if (instance == null) {
                instance = new ComparatorResource();
            }
            return instance;
        }
    }
    
}
