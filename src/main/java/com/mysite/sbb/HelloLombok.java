package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이랑 같이 사용(DI시 사용)
@Getter
public class HelloLombok {
    private final String hello;
    private final int lombok;

    // 롬복을 사용하지 않음 경우
//    public void setHello(String hello) {
//        this.hello = hello;
//    }
//
//    public void setLombok(int lombok) {
//        this.lombok = lombok;
//    }
//
//    public String getHello() {
//        return this.hello;
//    }
//
//    public int getLombok() {
//        return this.lombok;
//    }

    public static void main(String[] args) {
//        HelloLombok helloLombok = new HelloLombok();
//        helloLombok.setHello("헬로");
//        helloLombok.setLombok(5);
//
//        System.out.println(helloLombok.getHello());
//        System.out.println(helloLombok.getLombok());
    	
    	HelloLombok helloLombok = new HelloLombok("헬로", 5); 
        System.out.println(helloLombok.getHello()); 
        System.out.println(helloLombok.getLombok());
    }
}
