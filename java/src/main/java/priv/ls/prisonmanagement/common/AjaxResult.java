package priv.ls.prisonmanagement.common;


import com.google.gson.Gson;

/**
 * ajax 结果返回
 *
 * @author Calm
 * @created 2016年12月20日 下午5:36:15
 * @since v1.0
 */

public class AjaxResult<T> {
    private int code = 200;

    private String msg = "";

    private T results;

    private Page page;

    public AjaxResult() {
    }

    public AjaxResult(int code) {
        this.code = code;
    }

    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(int code, String msg, T result, Page page) {
        this.code = code;
        this.msg = msg;
        this.results = result;
        this.page = page;
    }

    public AjaxResult(T result) {
        this.code = 200;
        this.results = result;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}