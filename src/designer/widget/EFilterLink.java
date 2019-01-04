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

    public String toString() {
        String result = null;
        switch (this) {
            case equal:
                result ="=";
                break;
            case likeright:
                result = "*%";
                break;
            case likeleft:
                result = "%*";
                break;
            case like:
                result = "%%";
                break;

            default:
                break;

        }
       return  result;
    }


}
