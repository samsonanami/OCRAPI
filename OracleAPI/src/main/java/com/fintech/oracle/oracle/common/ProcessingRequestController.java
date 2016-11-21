package com.fintech.oracle.oracle.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * Created by sasitha on 11/21/16.
 */
@Controller
public class ProcessingRequestController {

    @RequestMapping(value = "v1/", method = RequestMethod.GET)
    @ResponseBody
    public Object serverStatus(HttpServletResponse response){
        return "server status : ok";
    }
}
