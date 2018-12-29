package designer.widget;

public enum EFilterLink {
    equal,like,likeleft,likeright,unknown;

    public static EFilterLink parse(String link) {
        if (link.equalsIgnoreCase("=")) {
            return  equal;
        } else if (link.equalsIgnoreCase("%%")) {
            return like;
        }else if (link.equalsIgnoreCase("%*")) {
            return likeleft;
        } else if (link.equalsIgnoreCase("*%")) {
            return likeright;
        } else {
            return unknown;
        }
    }


}
