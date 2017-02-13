/**
 * 
 */
package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;

import com.compera.portal.channel.ChannelElement;
import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.to.SeasonInfo;
import com.movile.labuenavida.util.Constants;
import com.movile.mob.portal.channel.ChannelLoaderProcess;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class ImagesProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List <ChannelElement> images = ChannelLoaderProcess.getChannelElements(Constants.SHOW_INFO.getCurrentSeason().getImagesChannelName());
        if (CollectionUtils.isNotEmpty(images)) {
            images = quicksort(images);
            Collections.reverse(images);
            request.setAttribute("images", images);
        }

        List<List<ChannelElement>> imagesOtherSeasons = new ArrayList<List<ChannelElement>>();
        List<String> stylesOtherSeasons = new ArrayList<String>();

        for (SeasonInfo season : Constants.SHOW_INFO.getSeasons()) {
            if (!Constants.SHOW_INFO.getCurrentSeason().getImagesChannelName().equals(season.getImagesChannelName())) {
                images = ChannelLoaderProcess.getChannelElements(season.getImagesChannelName());
                Collections.shuffle(images);
                imagesOtherSeasons.add(images.subList(0, 5));
                stylesOtherSeasons.add(season.getExclusiveCssClass() + "|" + season.getExclusiveImage());
            }
        }

        request.setAttribute("imagesOtherSeasons", imagesOtherSeasons);
        request.setAttribute("stylesOtherSeasons", stylesOtherSeasons);

        return new SuccessResult();
    }

    /**
     * This method sort the input ArrayList using quick sort algorithm.
     * @param input the ArrayList of integers.
     * @return sorted ArrayList of integers.
     */
    private List<ChannelElement> quicksort(List<ChannelElement> input){
        
        if(input.size() <= 1){
            return input;
        }
        
        int middle = (int) Math.ceil((double)input.size() / 2);
        ChannelElement pivot = input.get(middle);

        List<ChannelElement> less = new ArrayList<ChannelElement>();
        List<ChannelElement> greater = new ArrayList<ChannelElement>();
        
        for (int i = 0; i < input.size(); i++) {
            if(Integer.parseInt(input.get(i).getProperty("id").getSimpleValue()) <= Integer.parseInt(pivot.getProperty("id").getSimpleValue())){
                if(i == middle){
                    continue;
                }
                less.add(input.get(i));
            }
            else{
                greater.add(input.get(i));
            }
        }
        
        return concatenate(quicksort(less), pivot, quicksort(greater));
    }
    
    /**
     * Join the less array, pivot integer, and greater array
     * to single array.
     * @param less integer ArrayList with values less than pivot.
     * @param pivot the pivot integer.
     * @param greater integer ArrayList with values greater than pivot.
     * @return the integer ArrayList after join.
     */
    private List<ChannelElement> concatenate(List<ChannelElement> less, ChannelElement pivot, List<ChannelElement> greater){
        
        List<ChannelElement> list = new ArrayList<ChannelElement>();
        
        for (int i = 0; i < less.size(); i++) {
            list.add(less.get(i));
        }
        
        list.add(pivot);
        
        for (int i = 0; i < greater.size(); i++) {
            list.add(greater.get(i));
        }
        
        return list;
    }

}
