package hu.rka.talkfollow.results;

/**
 * Created by Réka on 2016.01.28..
 */
public class UserResult {

    String error;
    String msg;
    String token;

    public String getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public String getToken() {
        return token;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
