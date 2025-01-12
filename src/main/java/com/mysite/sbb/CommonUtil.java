package com.mysite.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component  //Spring Bean에서 사용하기 위한 어노테이션
public class CommonUtil {
    public String toMarkdown(String content){
        Parser parser = Parser.builder().build();   //Builder패턴을 사용하기 인스턴스 가져오기
        HtmlRenderer renderer = HtmlRenderer.builder().build(); //Builder패턴을 사용하기 인스턴스 가져오기
        Node document = parser.parse(content);

        return renderer.render(document);
    }
}
