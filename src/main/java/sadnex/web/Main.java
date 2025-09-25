package sadnex.web;

import sadnex.web.fcgi.RequestHandler;

public class Main {
    public static void main(String[] args) {
        new RequestHandler().start();
    }
}
