package com.movile.labuenavida.portal.processor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compera.portal.parser.bean.Item;
import com.compera.portal.processor.Processor;
import com.compera.portal.processor.result.CommitResult;
import com.compera.portal.processor.result.ProcessorResult;

public class PingProcessor extends Processor {
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    @Override
    public void initiate(Item item) {
    }

    @Override
    public void terminate() {
    }

    @Override
    public ProcessorResult process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String result = "OK";

            PrintWriter outGrade = response.getWriter();
            response.setContentLength(result.length());
            outGrade.print(result);
            outGrade.flush();
        } catch (Exception e) {
            EXCEPTION_LOGGER.error("[error : {" + e.getMessage() + "}]", e);
        }
        return new CommitResult();
    }

}
