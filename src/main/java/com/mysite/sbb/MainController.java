package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String root(){
        //System.out.println("메인 리스트로 이동");
        return "redirect:/question/list";
    }
}
