/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;

import com.compera.portal.channel.ChannelElement;
import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.util.Constants;
import com.movile.mob.portal.channel.ChannelLoaderProcess;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class ImagesRandomProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList <ChannelElement> images = (ArrayList<ChannelElement>)ChannelLoaderProcess.getChannelElements(Constants.SHOW_INFO.getCurrentSeason().getImagesChannelName());
        if (CollectionUtils.isNotEmpty(images)) {
            ArrayList <ChannelElement> imagesClone = (ArrayList <ChannelElement>)images.clone();
            if (images != null) {
                Collections.shuffle(imagesClone);
            }
            request.setAttribute("images", imagesClone);
        }
        return new SuccessResult();
    }

}
