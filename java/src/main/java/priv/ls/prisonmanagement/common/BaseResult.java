package priv.ls.prisonmanagement.common;

/**
 * 返回内容的封装
 */
public class BaseResult {

    private BaseResult() {
    }

    /**
     * 对返回数据进行包装，格式为：{"code":200,"msg":""}
     */
    public static AjaxResult successResult() {
        return new AjaxResult(ReturnCode.SUCCESS.getReturnCode());
    }

    public static AjaxResult successResult(Object obj) {
        return new AjaxResult(ReturnCode.SUCCESS.getReturnCode(), "", obj, null);
    }

    public static AjaxResult successResult(Object obj, Page page) {
        return new AjaxResult(ReturnCode.SUCCESS.getReturnCode(), "", obj, page);
    }

    public static AjaxResult failedResult(ReturnCode obj) {
        return new AjaxResult(obj.getReturnCode(), obj.getDesc(), null, null);
    }

    public static AjaxResult successResult(int code, String msg) {
        return new AjaxResult(code, msg);
    }

    /**
     * 对返回数据进行包装，格式为：{"code":输入的code,"msg":""}
     */
    public static AjaxResult failedResult(int code) {
        return new AjaxResult(code);
    }

    /**
     * 对返回数据进行包装，格式为：{"code":输入的code,"msg":"输入的msg"}
     */
    @SuppressWarnings("rawtypes")
    public static AjaxResult failedResult(int code, String msg) {
        return new AjaxResult(code, msg);
    }

    public static AjaxResult failedResult(int code, String msg, Object result) {
        return new AjaxResult(code, msg, result, null);
    }
}
