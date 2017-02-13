/**
 * 
 */
package com.movile.mob.portal.channel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.ProcessorResult;
import com.compera.portal.processor.result.SuccessResult;

/**
 * @author Peter Escamilla (peter.escamilla@movile.com)
 *
 */
public class ChannelLoaderProcessor extends Processor{

    @Override
    public void initiate(Item item) {}

    @Override
    public void terminate() {}

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new ChannelLoaderProcess().loadChannels("651");
        return new SuccessResult();
    }

}
