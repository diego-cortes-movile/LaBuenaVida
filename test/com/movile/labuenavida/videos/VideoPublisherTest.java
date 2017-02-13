/**
 * 
 */
package com.movile.labuenavida.videos;

import junit.framework.Assert;

import org.junit.Test;

import com.movile.labuenavida.videos.VideoPublisher;
import com.movile.labuenavida.videos.to.VideoResourceTO;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class VideoPublisherTest {

    /**
     * Test method for {@link com.movile.labuenavida.videos.VideoPublisher#getVideo(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVideoDownloadUrl() {
        VideoResourceTO videoExpected = new VideoResourceTO();
        videoExpected.setDownloadUrl("http://pd.videos.movile.com/core-media/vod/e7166750-4232-11e3-b875-1231381bf901_bitrate_300.mp4");
        videoExpected.setPreviewImg("http://preview.videos.movile.com/core-media/img/e7166750-4232-11e3-b875-1231381bf901_590.png");
        VideoResourceTO videoActual = VideoPublisher.getVideo("41791", null, "520");
        Assert.assertEquals(videoExpected, videoActual);
    }

}
