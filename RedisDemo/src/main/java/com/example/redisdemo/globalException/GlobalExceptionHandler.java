package com.example.redisdemo.globalException;

import com.example.redisdemo.returnCode.ResponseDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView customException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(ResponseDTO.succMsg("重复操作了！"));
        modelAndView.setViewName("override");
        return modelAndView;
    }
}
