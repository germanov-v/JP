package ru.yp.marketapp.adapters.web.controller.base;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public  interface CookieController {


    public static final String CART_COOKIE = "cartId";
    default void setCartCookie(HttpServletResponse response, long cartId, Long cartIdCookieId
                               ) {

        if(cartIdCookieId!=null&&cartIdCookieId==cartId){
            return;
        }



        var cookie = new Cookie(CART_COOKIE, Long.toString(cartId));

        cookie.setMaxAge(3600 * 30);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
