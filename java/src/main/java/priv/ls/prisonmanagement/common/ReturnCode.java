package priv.ls.prisonmanagement.common;

/**
 * 请求返回码
 */
public enum ReturnCode {
    /**
     * 阿里云通信
     */
    OK(1000, "OK"),
    // phone error
    ERROR_PHONE_NUM(1050, "非法手机号"),
    SEND_ERROR(1051, "短信发送失败"),
    PHONE_USED(1052, "手机号码已被注册"),
    PHONE_NEVER_USED(1053, "手机号码未注册"),
    ERROR_TEMPLATE_CODE(1054, "未知的短信模板"),
    ERROR_PHONE_SIZE(1055, "超出发送数量限制"),
    NULL_PHONE_SIZE(1056, "发送手机号码为空"),

    SUCCESS(200, "请求成功"),
    FAIL(555, "请求失败"),
    WRONG_INPUT(400, "输入错误"),
    TYPE_MISMATCH(400, "参数类型不匹配"),
    MISSING_SERVLET_REQUEST_PARAMETER(400, "请求参数缺失"),
    LOGIN_DENY(402, "账号已被管理员禁用"),
    CODE_WORING(401, "验证码错误"),
    CODE_NOT_SEND(402, "验证码未发送"),
    NOT_FIND_ACTION(404, "未找到Api"),
    RELOGIN_PLEASE(405,"请重新登录"),
    WX_ALREARY_IN_USER(419,"微信已经被使用"),
    WX_UNBAND(420,"微信没有绑定手机号码"),
    USERNAME_EXIST(421,"用户名已存在"),
    ACCOUNT_OR_PASSWORD_WRONG(422,"账号或密码错误"),
    TOO_MUCH_LOGIN(423,"登录频繁,已被禁止登录20分钟"),
    SYSTEM_ERROR(501, "系统错误"),
    SYSTEM_BUSY(503, "系统繁忙"),
    INVALID_SESSION(561, "无效的session"),
    UNAUTHORIZED(582, "未授权"),
    INVALID_PRIVILEGE(583, "无效的权限"),
    SYSTEM_BIZ_LIMIT(584, "业务受限"),
    UPLOAD_FAIL(585, "上传失败"),
    LOGIN_EXPIRED(600, "登录过期"),
    LOGIN_ELSEWHERE(601, "您的账号在其他地方登录"),
    REFLECT_ERROR(700, "实例化异常");

    private int returnCode;
    private String desc;

    ReturnCode(int returnCode, String desc) {
        this.returnCode = returnCode;
        this.desc = desc;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getDesc() {
        return desc;
    }
}
