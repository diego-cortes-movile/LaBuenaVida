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

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;
import com.movile.labuenavida.to.SeasonInfo;
import com.movile.labuenavida.util.Constants;
import com.movile.labuenavida.videos.VideoPublisher;
import com.movile.labuenavida.videos.to.VideoResourceTO;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class VideosProcessor extends Processor {

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<VideoResourceTO> videos = VideoPublisher.getAllVideos(Constants.SHOW_INFO.getCurrentSeason().getVideosCategoryId(), Constants.APPLICATION_ID);
        request.setAttribute("videos", videos);

        List<List<VideoResourceTO>> videosOtherSeasons = new ArrayList<List<VideoResourceTO>>();
        List<String> stylesOtherSeasons = new ArrayList<String>();

        for (SeasonInfo season : Constants.SHOW_INFO.getSeasons()) {
            if (!Constants.SHOW_INFO.getCurrentSeason().getVideosCategoryId().equals(season.getVideosCategoryId())) {
                videos = VideoPublisher.getAllVideos(season.getVideosCategoryId(), Constants.APPLICATION_ID);
                Collections.shuffle(videos);
                videosOtherSeasons.add(videos.subList(0, 5));
                stylesOtherSeasons.add(season.getExclusiveCssClass() + "|" + season.getExclusiveImage());
            }
        }

        request.setAttribute("videosOtherSeasons", videosOtherSeasons);
        request.setAttribute("stylesOtherSeasons", stylesOtherSeasons);

        return new SuccessResult();
    }

}
