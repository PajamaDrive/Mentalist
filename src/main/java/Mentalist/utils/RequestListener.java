package Mentalist.utils;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {
    public void requestDestroyed(ServletRequestEvent arg0) {}

    public void requestInitialized(ServletRequestEvent arg0) {
        ((HttpServletRequest)arg0.getServletRequest()).getSession();
    }
}